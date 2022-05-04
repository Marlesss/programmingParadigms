;; hard pls
;; hard pls
;; hard pls
(defn abs [x] (if (>= x 0) x (- x)))
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
(def _neutral (field :neutral))
(def toString (method :toString))
(def evaluate (method :evaluate))
(def diff (method :diff))
(defn Constant [value]
  {:value    value
   :toString (fn [this] (str (_value this)))
   :evaluate (fn [this _vars] (_value this))
   :diff     (fn [_this _var] (Constant 0))})
(defn Variable [value]
  {:value    value
   :toString _value
   :evaluate (fn [this vars] (get vars (_value this)))
   :diff     (fn [this var] (if (= (_value this) var) (Constant 1) (Constant 0)))})
; :NOTE: common constants should be extracted

(declare _Operation) ; :NOTE:/2 should've used declare instead of def
(def OperationProto
  {:toString (fn [this] (str "(" (_sign this) " " (clojure.string/join " " (map toString (_exprs this))) ")"))
   :evaluate (fn [this vars]
               (let [exprs (if (== (count (_exprs this)) 1) (conj (_exprs this) (Constant (_neutral this))) (_exprs this))]
                 (reduce (_calc this) (map #(evaluate %1 vars) exprs))))
   :diff     (fn [this var] (apply _Operation (_calc this) (_sign this) (_neutral this) (map #(diff %1 var) (_exprs this))))})
(defn Operation [this calc sign neutral & exprs]
  (assoc this
    :calc calc
    :sign sign
    :neutral neutral
    :exprs exprs))
(def _Operation (constructor Operation OperationProto))
(defn Add [& exprs] (apply _Operation + "+" 0 exprs))
(defn Subtract [& exprs] (apply _Operation - "-" 0 exprs))
(defn Multiply [& exprs]
  (assoc (apply _Operation * "*" 1 exprs)
    :diff (fn [this var]
            (if (== (count (_exprs this)) 1) (diff (first (_exprs this)) var)
                                             (Add (apply Multiply (diff (first (_exprs this)) var) (rest (_exprs this)))
                                                  (Multiply (diff (apply Multiply (rest (_exprs this))) var) (first (_exprs this))))))))
; :NOTE: needs to be fixed: support for 0 arguments for multiply and add
(defn Divide [& exprs]
  (assoc (apply _Operation (fn [^double x ^double y] (/ x y)) "/" 1 exprs)
    :diff (fn [this var] (let [exprs (if (== (count (_exprs this)) 1) (conj (_exprs this) (Constant (_neutral this))) (_exprs this))
                               first (first exprs) second (apply Multiply (rest exprs))]
                           (Divide (Subtract (Multiply (diff first var) second) (Multiply (diff second var) first))
                                   (Multiply second second))))))
(defn Negate [expr] (_Operation - "negate" 0 expr))
(defn Log [expr1 expr2]
  (assoc (_Operation #(/ (Math/log (abs %2)) (Math/log (abs %1))) "log" 0 expr1 expr2)
    :diff (fn [this var] (let [x (first (_exprs this)) y (nth (_exprs this) 1)] ; :NOTE:/2 double comparison is flawed
                           (if (and (contains? x :value) (number? (get x :value)) (== (get x :value) Math/E))
                             (Divide (diff y var) y)
                             (diff (Divide (Log (Constant Math/E) y) (Log (Constant Math/E) x)) var))))))
(defn Pow [expr1 expr2]
  (assoc (_Operation #(Math/pow %1 %2) "pow" 0 expr1 expr2)
    :diff (fn [this var] (let [x (first (_exprs this)) y (nth (_exprs this) 1)]
                           (Multiply this (diff (Multiply y (Log (Constant Math/E) x)) var))))))
(def funcs {"+" Add "-" Subtract "*" Multiply "/" Divide "negate" Negate "pow" Pow "log" Log})
(defn parser [inp] (if (seq? inp)
                     (apply (get funcs (name (first inp))) (mapv parser (rest inp)))
                     (if (number? inp) (Constant inp) (Variable (name inp)))))
(defn parseObject [inp] (parser (read-string inp)))
