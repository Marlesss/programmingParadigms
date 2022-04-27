;; hard pls
;; hard pls
;; hard pls
(defn less-eps [x] (<= (if (>= x 0) x (- x)) 1/1000))
(defn sameForm [& elems] (or (every? number? elems) (and (every? vector? elems) (apply == (mapv count elems))
                                                         (every? true? (apply mapv sameForm elems)))))
(defn isMatrix? [matr] (and (vector? matr) (vector? (first matr)) (every? vector? matr) (apply == (mapv count matr))))
(defn noNumbers? [elem] {:pre [(vector? elem)]} (if (empty? elem) true (and (vector? (first elem)) (every? noNumbers? elem))))
(defn forAllNumbers [f elem] {:pre [(or (number? elem) (vector? elem))] :post [(sameForm elem %)]}
  (if (number? elem) (f elem) (mapv (partial forAllNumbers f) elem)))
(defn v*s [v & scalars] {:pre [(vector? v) (every? number? v) (every? number? scalars)] :post [(sameForm % v)]}
  (mapv #(apply * % scalars) v))
(defn vop ([f vec] {:pre [(vector? vec)] :post [(sameForm % vec)]} (mapv f vec))
  ([f vec & vecs] {:pre  [(every? vector? (conj vecs vec)) (apply sameForm vec vecs)]
                   :post [(sameForm % vec)]} (reduce #(mapv f %1 %2) vec vecs)))
(defn v+ [& vecs] (apply vop + vecs))
(defn v- [& vecs] (apply vop - vecs))
(defn v* [& vecs] (apply vop * vecs))
(defn vd [& vecs] (apply vop / vecs))
(defn scalar [& vectors] {:pre [(every? vector? vectors) (apply == (mapv count vectors))] :post [(number? %)]} (apply + (apply v* vectors)))
(defn vect [& vecs] {:pre  [(every? vector? vecs) (apply == 3 (mapv count vecs)) (every? #(every? number? %) vecs)]
                     :post [(vector? %) (== (count %) 3) (every? number? %)]}
  (reduce #(vector (- (* (nth %1 1) (nth %2 2)) (* (nth %1 2) (nth %2 1)))
                   (- (* (nth %1 2) (nth %2 0)) (* (nth %1 0) (nth %2 2)))
                   (- (* (nth %1 0) (nth %2 1)) (* (nth %1 1) (nth %2 0)))) vecs))
(defn transpose [matr] {:pre  [(isMatrix? matr)]
                        :post [(or (and (isMatrix? %) (== (count %) (count (first matr))) (== (count (first %)) (count matr)))
                                   (and (noNumbers? matr) (= % [])))]}
  (mapv (fn [j] (mapv #(nth % j) matr)) (range 0 (count (first matr))))) ;; (apply mapv vector matr)
(defn m+ [& matrs] (apply vop v+ matrs))
(defn m- [& matrs] (apply vop v- matrs))
(defn m* [& matrs] (apply vop v* matrs))
(defn md [& matrs] (apply vop vd matrs))
(defn m*s [matr & scalars] {:pre [(isMatrix? matr) (every? number? scalars)] :post [(sameForm matr %)]} (forAllNumbers #(* % (apply * scalars)) matr))
(defn m*v [matr & vecs] {:pre [(isMatrix? matr) (every? vector? vecs)] :post [(vector? %) (== (count %) (count matr))]}
  (reduce #(mapv (fn [st] (apply + (v* st %2))) %1) matr vecs))
(defn m*m [& matrs]
  {:pre  [(every? isMatrix? matrs) (every? true? (for [i (range 0 (dec (count matrs)))]
                                                   (== (count (first (nth matrs i))) (count (nth matrs (inc i))))))]
   :post [(isMatrix? %) (== (count %) (count (first matrs))) (== (count (first %)) (count (first (last matrs))))]}
  (reduce (fn [m1 m2] (mapv #(m*v (transpose m2) %) m1)) matrs))
(defn shapelessop ([f elem] {:post [(sameForm elem %)]} (forAllNumbers f elem))
  ([f first & elems] {:pre [(apply sameForm first elems)] :post [(sameForm first %)]}
   (letfn [(shapelessbinary [s1 s2] {:pre [(sameForm s1 s2)] :post [(sameForm s1 %)]}
             (if (vector? s1) (mapv shapelessbinary s1 s2) (f s1 s2)))]
     (reduce shapelessbinary first elems))))
(defn s+ [& elems] (apply shapelessop + elems))
(defn s- [& elems] (apply shapelessop - elems))
(defn s* [& elems] (apply shapelessop * elems))
(defn sd [& elems] (apply shapelessop / elems))