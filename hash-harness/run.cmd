@ECHO OFF

SETLOCAL

SET CP=target\classes

SET JAVA_OPTS=-server -XX:+UnlockDiagnosticVMOptions -XX:+AggressiveOpts -XX:CompileThreshold=1000 -XX:-UseFastAccessorMethods
REM SET JAVA_OPTS=%JAVA_OPTS% -XX:+PrintCompilation -XX:+PrintInlining -XX:+PrintIntrinsics
REM SET JAVA_OPTS=%JAVA_OPTS% -XX:+PrintNMethods -XX:+DebugNonSafepoints -XX:+PrintAssembly

java -version

java %JAVA_OPTS% -cp "%CP%" org.kot.experiment.hash.TestRunner %*

ENDLOCAL
