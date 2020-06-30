
<p align="center">
  June 2020 BB Interview Assessment
</p>

<p align="center">
  <a href="https://github.com/mycaule/bb-assessment/actions"><img src="https://github.com/mycaule/bb-assessment/workflows/Scala%20CI/badge.svg?branch=master" alt="Build Status"></a>
  <br>
  <br>
</p>

### Usage
```
# Run the unit tests
sbt test
# Run the program
sbt run examples/program.mow
```

### Solution

A basic strategy to parse a file by pairs of lines that can be invalid is to try to extract a position or a list of instructions a each line. This can be extended to the case of a stream with lots of instructions produced continuously.

We suppose that the mower's coordinates don't change if we try to make it move outside of the allowed area.

With matrix notations, let *r(theta)* be a rotation matrix, *v(k)* a unit vector for direction, *p(k)* the position of the mower,
if we denote by *k* the current state, the next state *k+1* can be defined in the *(x, y)* coordinates system by:

```
           | [[ 0 1],
           | [-1 0]]    // if theta = -pi/2 (right rotation)
r(theta) = |
           | [[0 -1],
           | [1  0]]    // if theta = +pi/2 (left rotation)

v(k+1) = r(theta) @ v(k)  // shape (2, 1)
p(k+1) = p(k) + v(k+1)    // shape (2, 1)
```

### Code

I implemented an [`Mower`](src/main/scala/mowitnow/Mower.scala) object which is able to:
- parse a text file with instructions,
- write the data in the string format,
- compute the final positions with an iterative algorithm using the formulas above.

I also provided three files in the [examples folder](examples) for further testing.

```
sbt:mower> run examples/program.mow
[info] Running mowitnow.Mower examples/program.mow
Lawn of size 5x5
1 3 N
5 1 E
[success] Total time: 1 s, completed Jun 30, 2020 12:02:14 PM
```

The question of parallelization by threads is straightforward using [parallel collections](https://docs.scala-lang.org/overviews/parallel-collections/overview.html#fold).

```scala
import scala.collection.parallel.CollectionConverters._

// parse the input text file to a list of instructions,
// then add .par.fold to make the moves in parallel
```

The only problem is when the files are not perfectly valid.
In my implementation I dealt with the case where, see [`program.dirty.mow`](examples/program.dirty.mow) which can only be processed sequentially.

Multiple instances process of the same program can of course be run if the text files are splitted in a preliminary step.
