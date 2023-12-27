(benchmark file
  :extrafuns ((a Int) (b Int) (c Int) (d Real) (e Real))
  :assumption (> a (+ b 2))
  :assumption (= a (+ (* 2 c) 10))
  :assumption (<= (+ c b) 1000)
  :assumption (>= d e)
  :formula true)
