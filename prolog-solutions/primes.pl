next(X, X, X).
next(X, Y, R) :- X < Y, R is X.
next(X, Y, R) :- X < Y, next(X + 1, Y, R).

composite(X) :- X > 1, SqrtX is floor(sqrt(X)), 
	next(2, SqrtX + 1, I), 0 is mod(X, I).
prime(1) :- !, false.
prime(X) :- prime_table(X), !.
prime(X) :- \+composite(X), assert(prime_table(X)).

every_above(_, []).
every_above(X, [H | T]) :- (H > X; H is X), every_above(H, T).

multiply([], 1).
multiply([H | T], R) :- multiply(T, R1), R is R1 * H.

all_primes([]).
all_primes([H | T]) :- prime(H), all_primes(T).

prime_divisors(N, Divisors) :- prime_divisors_table(N, Divisors), !.
prime_divisors(1, Divisors) :- !, Divisors = [].
prime_divisors(N, Divisors) :- prime(N), !, Divisors = [N].
prime_divisors(N, [H | T]) :- number(H), !, multiply([H | T], N) , all_primes([H | T]), every_above(2, [H | T]),
	assert(prime_divisors_table(N, [H | T])).
prime_divisors(N, [H | T]) :- number(N), !, SqrtN is floor(sqrt(N)), next(2, SqrtN + 1, H), prime(H),
	0 is mod(N, H), NN is N / H, prime_divisors(NN, T), every_above(2, [H | T]),
	assert(prime_divisors_table(N, [H | T])).

listToSet([], []).
listToSet([H | T], Set) :- member(H, T), !, listToSet(T, Set).
listToSet([H | T], [H | T2]) :- listToSet(T, T2).

unique_prime_divisors(N, Divisors) :- prime_divisors(N, V), listToSet(V, Divisors).