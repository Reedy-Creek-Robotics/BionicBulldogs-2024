/*
add your functions here
format is 'Function' return type '_' argument types
return types:
  V = void
  C = bool but if true stops opmode
argument types:
  S = string
  D = double
  B = bool
  V = void
current macros are:
FunctionC_D
FunctionB_V
FunctionV_V
FunctionV_B
FunctionV_D
FunctionV_S
FunctionV_SS

Classes:
call NewClass to make a class
call EndClass with class name to end the class

Changing the current java object:
call SetJavaObject with the full name of the class using / as a seperator
*/

FunctionV_S(print, print);

FunctionC_D(delay, delay);
FunctionC_V(checkRunning, checkRunning);
FunctionB_V(isActive, isActive);
NewClass();
FunctionV_SS(telem, addData);
FunctionV_V(updateTelem, update);
EndClass(telem);

SetJavaObject(org/firstinspires/ftc/teamcode/modules/lua/TestModule);
NewClass();
FunctionV_D(setPos, setPos);
FunctionV_D(setPos2, setPos2);
EndClass(servos);