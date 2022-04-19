(defn abs [x] (if (>= x 0) x (- x)))
(defn less-eps [x] (<= (abs x) 1/1000))
(defn all [f elems] (reduce (fn [prev cur] (and prev cur)) (map f elems)))
(defn vop [f vecs]
  {:pre [(seq? vecs)
         (>= (count vecs) 2)
         (all vector? vecs)
         (all (fn [v] (== (count v) (count (first vecs)))) vecs)]}
  (if (> (count vecs) 2) (println vecs))
  (let [n (count (first vecs))]
    (letfn [(binvop [v1 v2]
              {:pre  [(vector? v1) (vector? v2) (== (count v1) n) (== (count v2) n)]
               :post [(vector? %) (== (count %) n)]}
              (into [] (for [i (range 0 n)] (f (nth v1 i) (nth v2 i)))))]
      (reduce (fn [v1 v2] (binvop v1 v2)) vecs))))
(defn v+ [& vecs] (vop + vecs))
(defn v- [& vecs] (vop - vecs))
(defn v* [& vecs] (vop * vecs))
(defn vd [& vecs] (vop / vecs))

(defn v*s [v & scalars]
  {:pre  [(vector? v)
          (not (zero? (count scalars)))
          (all number? scalars)]
   :post [(vector? %) (== (count v) (count %))]}
  (let [mnozh (reduce (fn [prev cur] (* prev cur)) scalars)]
    (mapv (fn [n] (* n mnozh)) v)))
(defn scalar [v1 v2]
  {:pre  [(vector? v1)
          (vector? v2)
          (== (count v1) (count v2))]
   :post [(number? %)]} (apply + (v* v1 v2)))
(defn vect [& vecs]
  {:pre  [(>= (count vecs) 2)
          (all vector? vecs)
          (all (fn [v] (== (count v) 3)) vecs)]
   :post [(vector? %)
          (== (count %) 3)]}
  (letfn [(binvect [v1 v2]
            {:pre  [(vector? v1) (vector? v2) (== (count v1) 3) (== (count v2) 3)]
             :post [(vector? %) (== (count %) 3) (less-eps (scalar v1 %)) (less-eps (scalar v2 %))]}
            [(- (* (nth v1 1) (nth v2 2)) (* (nth v1 2) (nth v2 1)))
             (- (* (nth v1 2) (nth v2 0)) (* (nth v1 0) (nth v2 2)))
             (- (* (nth v1 0) (nth v2 1)) (* (nth v1 1) (nth v2 0)))])]
    (reduce binvect vecs)))
(defn is-matrix [n m matr] (and (vector? matr) (== (count matr) n) (all vector? matr) (all (fn [v] (== (count v) m)) matr)))
(defn mop [f matrs]
  {:pre  [(seq? matrs)
          (>= (count matrs) 2)
          (vector? (first matrs))
          (vector? (first (first matrs)))
          (all (partial is-matrix (count (first matrs)) (count (first (first matrs)))) matrs)]
   :post [is-matrix (count (first matrs)) (count (first (first matrs))) %]}
  (let [n (count (first matrs))
        m (count (first (first matrs)))]
    (letfn [(binmop [m1 m2]
              {:pre  [(is-matrix n m m1)
                      (is-matrix n m m2)]
               :post [(is-matrix n m %)]}
              (into [] (for [i (range 0 n)] (f (nth m1 i) (nth m2 i)))))]
      (reduce (fn [m1 m2] (binmop m1 m2)) matrs))))
(defn m+ [& matrs] (vop v+ matrs))
(defn m- [& matrs] (vop v- matrs))
(defn m* [& matrs] (vop v* matrs))
(defn md [& matrs] (vop vd matrs))
(defn m*s [matr & scalars]
  {:pre  [(vector? matr)
          (vector? (first matr))
          (is-matrix (count matr) (count (first matr)) matr)
          (not (zero? (count scalars)))
          (all number? scalars)]
   :post [(is-matrix (count matr) (count (first matr)) %)]}
  (let [mnozh (reduce (fn [prev cur] (* prev cur)) scalars)]
    (mapv (fn [v] (mapv (fn [n] (* n mnozh)) v)) matr)))
(defn m*m [& matrs]
  {:pre  [(seq? matrs)
          (not (zero? (count matrs)))
          (all (fn [m] (and (vector? m) (vector? (first m)) (is-matrix (count m) (count (first m)) m))) matrs)]
   :post [(is-matrix (count (first matrs)) (count (first (last matrs))) %)]}
  (letfn [(binm*m [m1 m2]
            {:pre  [(vector? m1)
                    (vector? (first m1))
                    (is-matrix (count m1) (count (first m1)) m1)
                    (vector? m2)
                    (vector? (first m2))
                    (is-matrix (count m2) (count (first m2)) m2)
                    (== (count (first m1)) (count m2))]
             :post [is-matrix (count m1) (count (first m2)) %]}
            (let [n (count m1)
                  k (count (first m1))
                  m (count (first m2))]
              (into [] (for [i (range 0 n)] (into [] (for [j (range 0 m)] (reduce + (for [t (range 0 k)] (* (nth (nth m1 i) t) (nth (nth m2 t) j))))))))))]
    (reduce binm*m matrs)))
(defn m*v [matr & vecs]
  {:pre  [(vector? matr)
          (vector? (first matr))
          (is-matrix (count matr) (count (first matr)) matr)
          (seq? vecs)
          (all vector? vecs)]
   :post [(vector? %)
          (== (count %) (count matr))]}
  (reduce (fn [m v] (mapv (fn [st] (reduce + (v* st v))) m)) matr vecs))
(defn transpose [matr]
  {:pre  [(vector? matr)
          (vector? (first matr))
          (is-matrix (count matr) (count (first matr)) matr)]
   :post [(vector? %)
          (vector? (first %))
          (is-matrix (count (first matr)) (count matr) %)]}
  (let [n (count matr)
        m (count (first matr))]
    (into [] (for [j (range 0 m)] (into [] (for [i (range 0 n)] (nth (nth matr i) j)))))))
