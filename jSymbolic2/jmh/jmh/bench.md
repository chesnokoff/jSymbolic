1. test datasets (first step: single dataset)
2. run extraction of feature subsets on this datasets 
(first step: all features; all but histogram features; every single feature independently)
3. to do so we need Master-class with parameters: dataset path, output path, features to compute
4. implementation of Master-class can be dependent on library version
5. introduce Master-interface to accept construction parameters map (on constructor) + 
+ with method to run extraction

1. single-thread benchmark (run multiple times + warmup -> measurements)
2. multi-thread denchmark (requires clever dataset, beware of OOM, no big files/huge tick resolution)
3. huge file/resolutions run in single-threaded benchmark
4. (write test to split midi files)

+ flamegraph / flamechart (java profiling tools)