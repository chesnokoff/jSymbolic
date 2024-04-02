# Benchmark
This `jmh` folder was added for measuring execution time.

## How it works

It starts in Main class, where we create BenchmarkRunner with the help of BenchmarkRunnerBuilder.
It was decided to create BenchmarkBuilder class to easily change the default parameters of benchmarking


## Config Files
For specifying input midi files, output files, and features to extract, we use config files that already
are supported by jSymbolic2. An example of such file you can find in resources/jSymbolicDefaultConfigs.txt. Moreover, it's easy to generate such config files with 
Python scripts.

## Naming
For benchmark classes to run, let's have an agreement that such
classes ends with `...Benchmark`

## Branching
Template of jmh module is in `brenchmark` branch. If you want to benchmark your current version of
code, do next:
1) Checkout to commit that you want to test
2) Create new branch `benchmark-<id number>` and checkout to it
3) Cherry-pick commits from branch `benchmark`.
4) Create your benchmark classes

## Execution
The easiest way is:
1) Go to directory jSymbolic2
2) Run `./gradlew benchmarkJar`
3) Run `java -jar build/libs/jsymbolic2-benchmark-1.0-SNAPSHOT.jar` and it will run
benchmarks with default arguments

You can also run `java -jar build/libs/jsymbolic2-benchmark-1.0-SNAPSHOT.jar -h` to get
list of available parameters.

If you want to specify config file, run 
`java -jar build/libs/jsymbolic2-benchmark-1.0-SNAPSHOT.jar -c <path to config>`
