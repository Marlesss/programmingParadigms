(defn abs [x] (if (>= x 0) x (- x)))
(defn less-eps [x] (<= (abs x) 1/1000))
(defn all [f elems] (or (empty? elems) (reduce #(and %1 %2) (map f elems))))
(defn sameForm [s1 s2] (or (and (number? s1) (number? s2)) (and (vector? s1) (vector? s2) (== (count s1) (count s2)) (all true? (mapv sameForm s1 s2)))))
(defn is-matrix [n m matr] (and (vector? matr) (== (count matr) n) (all vector? matr) (all (fn [v] (== (count v) m)) matr)))
(defn vop [f vecs]
  {:pre  [(all vector? vecs) (all #(sameForm % (first vecs)) vecs)]
   :post [(sameForm % (first vecs))]}
  (reduce #(mapv f %1 %2) vecs))
(defn v+ [& vecs] (vop + vecs))
(defn v- [& vecs] (vop - vecs))
(defn v* [& vecs] (vop * vecs))
(defn vd [& vecs] (vop / vecs))

(defn v*s [v & scalars]
  {:pre  [(vector? v) (all number? scalars)]
   :post [(sameForm % v)]}
  (let [mnozh (apply * scalars)] (mapv #(* % mnozh) v)))
(defn scalar [v1 v2]
  {:pre  [(vector? v1) (vector? v2) (== (count v1) (count v2))]
   :post [(number? %)]}
  (apply + (v* v1 v2)))
(defn vect [& vecs]
  {:pre  [(all vector? vecs) (all #(== (count %) 3) vecs)]
   :post [(vector? %) (== (count %) 3)]}
  (letfn [(binvect [v1 v2]
            {:pre  [(vector? v1) (vector? v2) (== (count v1) 3) (== (count v2) 3)]
             :post [(vector? %) (== (count %) 3) (less-eps (scalar v1 %)) (less-eps (scalar v2 %))]}
            [(- (* (nth v1 1) (nth v2 2)) (* (nth v1 2) (nth v2 1)))
             (- (* (nth v1 2) (nth v2 0)) (* (nth v1 0) (nth v2 2)))
             (- (* (nth v1 0) (nth v2 1)) (* (nth v1 1) (nth v2 0)))])]
    (reduce binvect vecs)))
(defn m+ [& matrs] (vop v+ matrs))
(defn m- [& matrs] (vop v- matrs))
(defn m* [& matrs] (vop v* matrs))
(defn md [& matrs] (vop vd matrs))
(defn m*s [matr & scalars]
  {:pre  [(is-matrix (count matr) (count (first matr)) matr) (all number? scalars)]
   :post [(is-matrix (count matr) (count (first matr)) %)]}
  (let [mnozh (reduce (fn [prev cur] (* prev cur)) scalars)]
    (mapv (fn [v] (mapv (fn [n] (* n mnozh)) v)) matr)))
(defn m*m [& matrs]
  {:pre  [(all #(is-matrix (count %) (count (first %)) %) matrs)
          (all true? (for [i (range 0 (dec (count matrs)))] (== (count (first (nth matrs i))) (count (nth matrs (inc i))))))]
   :post [(is-matrix (count (first matrs)) (count (first (last matrs))) %)]}
  (letfn [(binm*m [m1 m2]
            {:pre  [(is-matrix (count m1) (count (first m1)) m1) (is-matrix (count m2) (count (first m2)) m2) (== (count (first m1)) (count m2))]
             :post [is-matrix (count m1) (count (first m2)) %]}
            (let [n (count m1) k (count (first m1)) m (count (first m2))]
              (into [] (for [i (range 0 n)] (into [] (for [j (range 0 m)] (reduce + (for [t (range 0 k)] (* (nth (nth m1 i) t) (nth (nth m2 t) j))))))))))]
    (reduce binm*m matrs)))
(defn m*v [matr & vecs]
  {:pre  [(is-matrix (count matr) (count (first matr)) matr) (all vector? vecs)]
   :post [(vector? %) (== (count %) (count matr))]}
  (reduce #(mapv (fn [st] (reduce + (v* st %2))) %1) matr vecs))
(defn transpose [matr]
  {:pre  [(is-matrix (count matr) (count (first matr)) matr)]
   :post [(is-matrix (count (first matr)) (count matr) %)]}
  (let [n (count matr) m (count (first matr))]
    (into [] (for [j (range 0 m)] (into [] (for [i (range 0 n)] (nth (nth matr i) j)))))))
(defn shapelessop [f elems]
  {:pre  [(all (partial sameForm (first elems)) elems)]
   :post [(sameForm (first elems) %)]}
  (letfn [(shapelessbinary [s1 s2] {:pre [(sameForm s1 s2)] :post [(sameForm s1 %)]}
            (if (vector? s1) (mapv shapelessbinary s1 s2) (f s1 s2)))]
    (reduce shapelessbinary elems)))
(defn s+ [& elems] (shapelessop + elems))
(defn s- [& elems] (shapelessop - elems))
(defn s* [& elems] (shapelessop * elems))
(defn sd [& elems] (shapelessop / elems))