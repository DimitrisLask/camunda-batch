@echo off

cd batchcontroller-app\bin\

echo Starting Batch Controller...

start batchcontroller.bat

cd ..\..

cd camunda-bpm-platform-app\

echo Starting Camunda BPM...

start start-camunda.bat

cd ..

cd BatchManagement-app\app\

start index.html