(defn less-eps [x] (<= (if (>= x 0) x (- x)) 1/1000))
(defn all [f elems] (or (empty? elems) (reduce #(and %1 %2) (map f elems))))
(defn sameForm [first & others] {:pre [(or (all number? (conj others first)) (all vector? (conj others first)))] :post [(true? %)]}
  (letfn [(binSameForm [s1 s2] (or (and (number? s1) (number? s2))
                                   (and (vector? s1) (vector? s2) (== (count s1) (count s2)) (all true? (mapv binSameForm s1 s2)))))]
    (all (partial binSameForm first) others)))
(defn isMatrix [matr] (and (vector? matr) (vector? (first matr)) (all vector? matr) (apply == (mapv count matr))))
(defn noNumbers [elem] {:pre [(vector? elem)]} (if (empty? elem) true (and (vector? (first elem)) (all noNumbers elem))))
(defn forAllNumbers [f elem] {:pre [(or (number? elem) (vector? elem))] :post [(sameForm elem %)]}
  (if (number? elem) (f elem) (mapv (partial forAllNumbers f) elem)))
(defn v*s [v & scalars] {:pre [(vector? v) (all number? scalars)] :post [(sameForm % v)]} (mapv #(* % (apply * scalars)) v))
(defn vop ([f vec] {:pre [(vector? vec)] :post [(sameForm % vec)]} (mapv f vec))
  ([f vec & vecs] {:pre [(all vector? (conj vecs vec)) (apply sameForm vec vecs)] :post [(sameForm % vec)]} (reduce #(mapv f %1 %2) vec vecs)))
(defn v+ [& vecs] (apply vop + vecs))
(defn v- [& vecs] (apply vop - vecs))
(defn v* [& vecs] (apply vop * vecs))
(defn vd [& vecs] (apply vop / vecs))
(defn scalar [& vectors] {:pre [(all vector? vectors) (apply == (mapv count vectors))] :post [(number? %)]} (apply + (apply v* vectors)))
(defn vect [& vecs] {:pre  [(all vector? vecs) (apply == 3 (mapv count vecs)) (all true? (mapv #(all number? %) vecs))]
                     :post [(vector? %) (== (count %) 3) (all number? %)]}
  (reduce #(vector (- (* (nth %1 1) (nth %2 2)) (* (nth %1 2) (nth %2 1)))
                   (- (* (nth %1 2) (nth %2 0)) (* (nth %1 0) (nth %2 2)))
                   (- (* (nth %1 0) (nth %2 1)) (* (nth %1 1) (nth %2 0)))) vecs))
(defn m+ [& matrs] (apply vop v+ matrs))
(defn m- [& matrs] (apply vop v- matrs))
(defn m* [& matrs] (apply vop v* matrs))
(defn md [& matrs] (apply vop vd matrs))
(defn m*s [matr & scalars] {:pre [(isMatrix matr) (all number? scalars)] :post [(sameForm matr %)]} (forAllNumbers #(* % (apply * scalars)) matr))
(defn m*m [& matrs]
  {:pre  [(all isMatrix matrs) (all true? (for [i (range 0 (dec (count matrs)))] (== (count (first (nth matrs i))) (count (nth matrs (inc i))))))] ;; нужно ли добавлять второе pre условие, если оно есть в binm*m?
   :post [(isMatrix %) (== (count %) (count (first matrs))) (== (count (first %)) (count (first (last matrs))))]}
  (letfn [(binm*m [m1 m2] {:pre  [(isMatrix m1) (isMatrix m2) (== (count (first m1)) (count m2))]
                           :post [(isMatrix %) (== (count %) (count m1)) (== (count (first %)) (count (first m2)))]}
            (let [n (count m1) k (count (first m1)) m (count (first m2))]
              (into [] (for [i (range 0 n)] (into [] (for [j (range 0 m)] (reduce + (for [t (range 0 k)] (* (nth (nth m1 i) t) (nth (nth m2 t) j))))))))))]
    (reduce binm*m matrs)))
(defn m*v [matr & vecs] {:pre [(isMatrix matr) (all vector? vecs)] :post [(vector? %) (== (count %) (count matr))]}
  (reduce #(mapv (fn [st] (reduce + (v* st %2))) %1) matr vecs))
(defn transpose [matr] {:pre  [(isMatrix matr)]
                        :post [(or (and (isMatrix %) (== (count %) (count (first matr))) (== (count (first %)) (count matr)))
                                   (and (noNumbers matr) (= % [])))]}
  (let [n (count matr) m (count (first matr))]
    (into [] (for [j (range 0 m)] (into [] (for [i (range 0 n)] (nth (nth matr i) j)))))))
(defn shapelessop ([f elem] {:post [(sameForm elem %)]} (forAllNumbers f elem))
  ([f first & elems]
   {:pre [(apply sameForm first elems)] :post [(sameForm first %)]}
   (letfn [(shapelessbinary [s1 s2] {:pre [(sameForm s1 s2)] :post [(sameForm s1 %)]}
             (if (vector? s1) (mapv shapelessbinary s1 s2) (f s1 s2)))]
     (reduce shapelessbinary first elems))))
(defn s+ [& elems] (apply shapelessop + elems))
(defn s- [& elems] (apply shapelessop - elems))
(defn s* [& elems] (apply shapelessop * elems))
(defn sd [& elems] (apply shapelessop / elems))