;; hard pls
;; hard pls
;; hard pls
(defn proto-get
  ([obj key] (proto-get obj key nil))
  ([obj key default]
   (cond
     (contains? obj key) (obj key)
     (contains? obj :prototype) (proto-get (obj :prototype) key default)
     :else default)))
(defn proto-call [this key & args] (apply (proto-get this key) this args))
(defn field [key] (fn
                    ([this] (proto-get this key))
                    ([this def] (proto-get this key def))))
(defn method [key] (fn [this & args] (apply proto-call this key args)))
(defn constructor [ctor prototype] (fn [& args] (apply ctor {:prototype prototype} args)))
(def _sign (field :sign))
(def _calc (field :calc))
(def _exprs (field :exprs))
(def _value (field :value))
(def toString (method :toString))
(def evaluate (method :evaluate))
(def OperationProto
  {:toString (fn [this] (str "(" (_sign this) " " (clojure.string/join " " (map toString (_exprs this))) ")"))
   :evaluate (fn [this] (fn [map] (apply _calc this (mapv evaluate _exprs))))
   })
(defn Operation [this calc & exprs]
  (assoc this
    :calc calc
    :exprs exprs))
(def _Operation (constructor Operation OperationProto))
(defn Constant [value]
  {:value    value
   :toString (fn [this] (str value))
   :evaluate (constantly value)})
(defn Variable [value]
  {:value    value
   :toString _value
   :evaluate (fn [this] (fn [map & args] (println "GOT: " map "|" args) (get map name)))})
;(defn constant [c] (constantly c))
;(defn variable [var] (fn [map] (get map var)))
;(defn operation [f neutral & exprs]
;  (let [exprs (if (== (count exprs) 1) (conj exprs (constant neutral)) exprs)]
;    (fn [map] (reduce f (mapv #(% map) exprs)))))
;(defn add [& exprs] (apply operation + 0 exprs))
;(defn subtract [& exprs] (apply operation - 0 exprs))
;(defn multiply [& exprs] (apply operation * 1 exprs))
;(defn divide [& exprs] (apply operation (fn [^double x ^double y] (/ x y)) 1 exprs)) ;; (apply / [1.0 0.0])
;(defn negate [expr] (subtract expr))
;(defn mean [& exprs] (divide (apply add exprs) (constant (count exprs))))
;(defn varn [& exprs] (subtract (apply mean (mapv #(multiply % %) exprs)) (#(multiply % %) (apply mean exprs))))
;(defn pow [expr1 expr2] (operation #(java.lang.Math/pow %1 %2) 0 expr1 expr2))
;(defn abs [x] (if (>= x 0) x (- x)))
;(defn log [expr1 expr2] (operation #(/ (java.lang.Math/log (abs %2)) (java.lang.Math/log (abs %1))) 0 expr1 expr2))
;(def funcs {"+" add "-" subtract "*" multiply "/" divide "negate" negate "mean" mean "varn" varn "pow" pow "log" log})
;(defn parser [inp] (if (seq? inp)
;                     (apply (get funcs (name (first inp))) (mapv parser (rest inp)))
;                     (if (number? inp) (constant inp) (variable (name inp)))))
;(defn parseFunction [inp] (parser (read-string inp)))
(println "cons")
(def const (Constant 5))
(println const)
(println (evaluate const {"x" 5}))
(println (evaluate const {"x" 6}))
(println (evaluate const {"x" 7}))
(println "variable")
(def variable (Variable "x"))
(println variable)
(println (evaluate variable {"x" 5}))
(println (evaluate variable {"x" 6}))
(println (evaluate variable {"x" 7}))