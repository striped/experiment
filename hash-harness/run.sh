#!/bin/sh

CP=target/classes

JAVA_OPTS='-server -XX:+UnlockDiagnosticVMOptions -XX:+AggressiveOpts -XX:CompileThreshold=1000 -XX:-UseFastAccessorMethods'
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintCompilation -XX:+PrintInlining -XX:+PrintIntrinsics"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintNMethods -XX:+DebugNonSafepoints -XX:+PrintAssembly"

java -version

java $JAVA_OPTS -cp "$CP" org.kot.experiment.hash.TestRunner $@
