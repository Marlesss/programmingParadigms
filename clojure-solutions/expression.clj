;; hard pls
;; hard pls
;; hard pls
(defn constant [c] (constantly c))
(defn variable [var] (fn [map] (get map var)))
(defn operation [f neutral & exprs]
  (let [exprs (if (== (count exprs) 1) (conj exprs (constant neutral)) exprs)]
    (fn [map] (reduce f (mapv #(% map) exprs)))))
(defn add [& exprs] (apply operation + 0 exprs))
(defn subtract [& exprs] (apply operation - 0 exprs))
(defn multiply [& exprs] (apply operation * 1 exprs))
(defn divide [& exprs] (apply operation (fn [^double x ^double y] (/ x y)) 1 exprs)) ;; (apply / [1.0 0.0])
(defn negate [expr] (subtract expr))
(defn mean [& exprs] (divide (apply add exprs) (constant (count exprs))))
(defn varn [& exprs] (subtract (apply mean (mapv #(multiply % %) exprs)) (#(multiply % %) (apply mean exprs))))
(defn pow [expr1 expr2] (operation #(java.lang.Math/pow %1 %2) 0 expr1 expr2))
(defn abs [x] (if (>= x 0) x (- x)))
(defn log [expr1 expr2] (operation #(/ (java.lang.Math/log (abs %2)) (java.lang.Math/log (abs %1))) 0 expr1 expr2))
(def funcs {"+" add "-" subtract "*" multiply "/" divide "negate" negate "mean" mean "varn" varn "pow" pow "log" log})
(defn parser [inp] (if (seq? inp)
                     (apply (get funcs (name (first inp))) (mapv parser (rest inp)))
                     (if (number? inp) (constant inp) (variable (name inp)))))
(defn parseFunction [inp] (parser (read-string inp)))