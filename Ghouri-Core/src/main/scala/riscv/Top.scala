package riscv
import chisel3._

class Top extends Module{
	val io = IO(new Bundle {
		//val instruction = Input(UInt(32.W))
		//val pc = Input(UInt(32.W))
		val regout = Output(SInt(32.W))
		
		

	})
	
	val Instruction = Module(new Instruction())
	val pc = Module(new Pc())
	val control = Module(new Control())
	val immediate = Module(new Imme())
	val alu = Module(new Alu())
	val alucontrol = Module(new AluOp())
	val regfile = Module(new Regfile())
	val jalr = Module(new Jalrr())
	val memory = Module(new Memory())
	Instruction.io.wrAddr := pc.io.pc(11,2)
	val instruction = Instruction.io.rdData
	val jalout = jalr.io.out
	val bout = control.io.branch & alu.io.branch
	
	when(control.io.nxtpc === "b00".U){
		pc.io.input := pc.io.pc4
	}.elsewhen(control.io.nxtpc === "b11".U){
		pc.io.input := jalout
	}.elsewhen(control.io.nxtpc === "b10".U){
		pc.io.input := immediate.io.Ujout.asUInt
	}.elsewhen(control.io.nxtpc === "b01".U){
		when(bout === 1.S){
			pc.io.input := immediate.io.Sbout.asUInt
		}.otherwise{
			pc.io.input := DontCare
		}
	}.otherwise{
		pc.io.input := DontCare
	}


	control.io.opcode := instruction(6,0)
	immediate.io.inst := instruction
	immediate.io.pc := pc.io.pc
	

	regfile.io.r1 := instruction(19,15)
	regfile.io.r2 := instruction(24,20)
	regfile.io.rd_sel := instruction(11,7)
	regfile.io.regwrite := control.io.regwrite

	alucontrol.io.aluop := control.io.aluop
	alucontrol.io.fnc3 := instruction(14,12)
	alucontrol.io.fnc7 := instruction(30)


	when((control.io.opA === "b00".U) || (control.io.opA === "b11".U)){
		alu.io.rs1 := regfile.io.rs1
	}.elsewhen(control.io.opA === "b10".U){
		alu.io.rs1 := pc.io.pc4.asSInt
	}.otherwise{
		alu.io.rs1 := DontCare
	}
	
	jalr.io.input1 := regfile.io.rs1
	when((control.io.extend === "b00".U) && (control.io.nxtpc === "b11".U)){
		jalr.io.input2 := immediate.io.Iout
	}.otherwise{
		jalr.io.input2 := DontCare
	}
	


	when(control.io.extend === "b00".U && control.io.opB === "b1".U){
		alu.io.rs2 := immediate.io.Iout
	}.elsewhen(control.io.extend === "b10".U && control.io.opB === "b1".U){
		alu.io.rs2 := immediate.io.Sout
	}.elsewhen(control.io.extend === "b01".U && control.io.opB === "b1".U){
		alu.io.rs2 := immediate.io.Uout
	}.otherwise{
		alu.io.rs2 := regfile.io.rs2
	}

	memory.io.load := control.io.memread
	memory.io.store := control.io.memwrite
	memory.io.data := regfile.io.rs2
	memory.io.addd := alu.io.out(9,2).asUInt

	alu.io.cntrl := alucontrol.io.out
	when(control.io.memtoreg === "b1".U){
		regfile.io.writedata := memory.io.out
	}.otherwise{
		regfile.io.writedata := alu.io.out
	}
	
	io.regout := regfile.io.writedata
}



