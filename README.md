# RISC-V Single Cycle Core Ghouri
### Designed by Mohammad Mohsin Raza
To Clone this repository on your machine, open terminal and type the following command.  
```ruby
git clone https://github.com/merledu/Ghouri-Core.git
```
Now create a .txt file of hexadecimal code of your instructions\
like this:
```
00400113
00500193
010000EF
00402223
00402283
00520663
40310233
00008067
```
Then open Instruction.scala from core/Ghouri/src/main/scala/riscv\
Edit this line and replace the current path with your instruction text file path
``` python
loadMemoryFromFile(mem, "/home/mohsin/inst")
```
After saving the instruction file go inside Ghouri folder from terminal
```ruby
cd core/Ghouri-Core
```
And enter
```ruby
sbt
```
When the terminal shows this
```ruby
sbt:Ghouri-Core>
```
Then enter this command
```ruby
sbt:Ghouri-Core> test:runMain riscv.Launcher Top
```
After you get success, run
```ruby
sbt:Ghouri-Core> test:runMain riscv.Launcher Top --backend-name verilator
```
After Success line Open the folder (test_run_dir) then go to examples > Top > Top.vcd, with this file\
 you can view your program running on gtkwave.
 
