;; hard pls
;; hard pls
;; hard pls
(defn abs [x] (if (>= x 0) x (- x)))
(defn less-eps [x] (<= (if (>= x 0) x (- x)) 1/1000))
(defn divideDouble
  ([^double x] (/ 1 x))
  ([first & args] (reduce (fn [^double x ^double y] (/ x y)) first args)))
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
(def diff (method :diff))
(declare Constant0)
(defn Constant [value]
  {:value    value
   :toString (fn [this] (str (_value this)))
   :evaluate (fn [this _vars] (_value this))
   :diff     (fn [_this _var] Constant0)})
(def Constant0 (Constant 0))
(def Constant1 (Constant 1))
(def ConstantE (Constant Math/E))
(defn Variable [value]
  {:value    value
   :toString _value
   :evaluate (fn [this vars] (get vars (_value this)))
   :diff     (fn [this var] (if (= (_value this) var) Constant1 Constant0))})
; :FIXED: :NOTE: common constants should be extracted

(declare _Operation)                                        ; :FIXED: :NOTE:/2 should've used declare instead of def
(def OperationProto
  {:toString (fn [this] (str "(" (_sign this) " " (clojure.string/join " " (map toString (_exprs this))) ")"))
   :evaluate (fn [this vars]
               (let [exprs (_exprs this)]
                 (apply (_calc this) (map #(evaluate %1 vars) exprs))))
   :diff     (fn [this var] (apply _Operation (_calc this) (_sign this) (map #(diff %1 var) (_exprs this))))})
(defn Operation [this calc sign & exprs]
  (assoc this
    :calc calc
    :sign sign
    :exprs exprs))
(def _Operation (constructor Operation OperationProto))
(defn Add [& exprs] (apply _Operation + "+" exprs))
(defn Subtract [& exprs] (apply _Operation - "-" exprs))
(defn Multiply [& exprs]
  (assoc (apply _Operation * "*" exprs)
    :diff (fn [this var]
            (cond
              (== (count (_exprs this)) 0) Constant0        ; (*) == 1 --> (*)` == 1` == 0
              (== (count (_exprs this)) 1) (diff (first (_exprs this)) var)
              :else (Add (apply Multiply (diff (first (_exprs this)) var) (rest (_exprs this)))
                         (Multiply (diff (apply Multiply (rest (_exprs this))) var) (first (_exprs this))))))))
; :FIXED: :NOTE: needs to be fixed: support for 0 arguments for multiply and add
(defn Divide [& exprs]
  (assoc (apply _Operation divideDouble "/" exprs)
    :diff (fn [this var] (let [exprs (if (== (count (_exprs this)) 1) (conj (_exprs this) Constant1) (_exprs this))
                               first (first exprs) second (apply Multiply (rest exprs))]
                           (Divide (Subtract (Multiply (diff first var) second) (Multiply (diff second var) first))
                                   (Multiply second second))))))
(defn Negate [expr] (_Operation - "negate" expr))
(defn Log [expr1 expr2]
  (assoc (_Operation #(/ (Math/log (abs %2)) (Math/log (abs %1))) "log" expr1 expr2)
    :diff (fn [this var] (let [x (first (_exprs this)) y (nth (_exprs this) 1)] ; :FIXED: :NOTE:/2 double comparison is flawed
                           (if (and (contains? x :value) (number? (get x :value)) (less-eps (- (get x :value) Math/E)))
                             (Divide (diff y var) y)
                             (diff (Divide (Log ConstantE y) (Log ConstantE x)) var))))))
(defn Pow [expr1 expr2]
  (assoc (_Operation #(Math/pow %1 %2) "pow" expr1 expr2)
    :diff (fn [this var] (let [x (first (_exprs this)) y (nth (_exprs this) 1)]
                           (Multiply this (diff (Multiply y (Log ConstantE x)) var))))))
(def Objective {"constant" Constant "variable" Variable "+" Add "-" Subtract "*" Multiply "/" Divide
                "negate"   Negate "pow" Pow "log" Log})

;(println (evaluate (Add) {})) ; >> 0
;(println (evaluate (Multiply) {})) ; >> 1
;(println (evaluate (Subtract) {})) ; >> Wrong number of args
;(println (evaluate (Divide) {})) ; >> Wrong number of args


; --- Functional ---

(defn constant [c] (constantly c))
(defn variable [var] (fn [map] (get map var)))
(defn operation [f & exprs] (fn [map] (apply f (mapv #(% map) exprs))))
(defn add [& exprs] (apply operation + exprs))
(defn subtract [& exprs] (apply operation - exprs))
(defn multiply [& exprs] (apply operation * exprs))
(defn divide [& exprs] (apply operation divideDouble exprs))
(defn negate [expr] (subtract expr))
(defn mean [& exprs] (divide (apply add exprs) (constant (count exprs))))
(defn varn [& exprs] (subtract (apply mean (mapv #(multiply % %) exprs)) (#(multiply % %) (apply mean exprs))))
(defn pow [expr1 expr2] (operation #(Math/pow %1 %2) expr1 expr2))
(defn log [expr1 expr2] (operation #(/ (Math/log (abs %2)) (Math/log (abs %1))) expr1 expr2))
(def Functional {"constant" constant "variable" variable "+" add "-" subtract "*" multiply "/" divide
                 "negate"   negate "mean" mean "varn" varn "pow" pow "log" log})
(defn parser [inp, constr] (if (seq? inp)
                             (apply (get constr (name (first inp))) (mapv #(parser % constr) (rest inp)))
                             (if (number? inp) ((get constr "constant") inp) ((get constr "variable") (name inp)))))
(defn parseObject [inp] (parser (read-string inp), Objective))
(defn parseFunction [inp] (parser (read-string inp), Functional))
