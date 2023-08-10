	.text
	.attribute	4, 16
	.attribute	5, "rv32i2p0"
	.file	"builtin.c"
	.globl	print                           # -- Begin function print
	.p2align	1
	.type	print,@function
print:                                  # @print
# %bb.0:
	lui	a1, %hi(.L.str)
	addi	a1, a1, %lo(.L.str)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end0:
	.size	print, .Lfunc_end0-print
                                        # -- End function
	.globl	println                         # -- Begin function println
	.p2align	1
	.type	println,@function
println:                                # @println
# %bb.0:
	lui	a1, %hi(.L.str.1)
	addi	a1, a1, %lo(.L.str.1)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end1:
	.size	println, .Lfunc_end1-println
                                        # -- End function
	.globl	printInt                        # -- Begin function printInt
	.p2align	1
	.type	printInt,@function
printInt:                               # @printInt
# %bb.0:
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end2:
	.size	printInt, .Lfunc_end2-printInt
                                        # -- End function
	.globl	printlnInt                      # -- Begin function printlnInt
	.p2align	1
	.type	printlnInt,@function
printlnInt:                             # @printlnInt
# %bb.0:
	lui	a1, %hi(.L.str.3)
	addi	a1, a1, %lo(.L.str.3)
	mv	a2, a0
	mv	a0, a1
	mv	a1, a2
	tail	printf
.Lfunc_end3:
	.size	printlnInt, .Lfunc_end3-printlnInt
                                        # -- End function
	.globl	getString                       # -- Begin function getString
	.p2align	1
	.type	getString,@function
getString:                              # @getString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	li	a0, 256
	call	malloc
	mv	s0, a0
	lui	a0, %hi(.L.str)
	addi	a0, a0, %lo(.L.str)
	mv	a1, s0
	call	scanf
	mv	a0, s0
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end4:
	.size	getString, .Lfunc_end4-getString
                                        # -- End function
	.globl	getInt                          # -- Begin function getInt
	.p2align	1
	.type	getInt,@function
getInt:                                 # @getInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	lui	a0, %hi(.L.str.2)
	addi	a0, a0, %lo(.L.str.2)
	addi	a1, sp, 8
	call	scanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end5:
	.size	getInt, .Lfunc_end5-getInt
                                        # -- End function
	.globl	toString                        # -- Begin function toString
	.p2align	1
	.type	toString,@function
toString:                               # @toString
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	sw	s0, 8(sp)                       # 4-byte Folded Spill
	sw	s1, 4(sp)                       # 4-byte Folded Spill
	mv	s0, a0
	li	a0, 16
	call	malloc
	mv	s1, a0
	lui	a0, %hi(.L.str.2)
	addi	a1, a0, %lo(.L.str.2)
	mv	a0, s1
	mv	a2, s0
	call	sprintf
	mv	a0, s1
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	lw	s0, 8(sp)                       # 4-byte Folded Reload
	lw	s1, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end6:
	.size	toString, .Lfunc_end6-toString
                                        # -- End function
	.globl	string.length                   # -- Begin function string.length
	.p2align	1
	.type	string.length,@function
string.length:                          # @string.length
# %bb.0:
	tail	strlen
.Lfunc_end7:
	.size	string.length, .Lfunc_end7-string.length
                                        # -- End function
	.globl	string.substring                # -- Begin function string.substring
	.p2align	1
	.type	string.substring,@function
string.substring:                       # @string.substring
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	sw	s1, 20(sp)                      # 4-byte Folded Spill
	sw	s2, 16(sp)                      # 4-byte Folded Spill
	sw	s3, 12(sp)                      # 4-byte Folded Spill
	mv	s3, a1
	mv	s2, a0
	sub	s1, a2, a1
	addi	a0, s1, 1
	call	malloc
	mv	s0, a0
	add	a1, s2, s3
	mv	a2, s1
	call	memcpy
	add	a0, s0, s1
	sb	zero, 0(a0)
	mv	a0, s0
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	lw	s1, 20(sp)                      # 4-byte Folded Reload
	lw	s2, 16(sp)                      # 4-byte Folded Reload
	lw	s3, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end8:
	.size	string.substring, .Lfunc_end8-string.substring
                                        # -- End function
	.globl	string.parseInt                 # -- Begin function string.parseInt
	.p2align	1
	.type	string.parseInt,@function
string.parseInt:                        # @string.parseInt
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	lui	a1, %hi(.L.str.2)
	addi	a1, a1, %lo(.L.str.2)
	addi	a2, sp, 8
	call	sscanf
	lw	a0, 8(sp)
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end9:
	.size	string.parseInt, .Lfunc_end9-string.parseInt
                                        # -- End function
	.globl	string.ord                      # -- Begin function string.ord
	.p2align	1
	.type	string.ord,@function
string.ord:                             # @string.ord
# %bb.0:
	add	a0, a0, a1
	lbu	a0, 0(a0)
	ret
.Lfunc_end10:
	.size	string.ord, .Lfunc_end10-string.ord
                                        # -- End function
	.globl	string.add                      # -- Begin function string.add
	.p2align	1
	.type	string.add,@function
string.add:                             # @string.add
# %bb.0:
	addi	sp, sp, -32
	sw	ra, 28(sp)                      # 4-byte Folded Spill
	sw	s0, 24(sp)                      # 4-byte Folded Spill
	sw	s1, 20(sp)                      # 4-byte Folded Spill
	sw	s2, 16(sp)                      # 4-byte Folded Spill
	sw	s3, 12(sp)                      # 4-byte Folded Spill
	sw	s4, 8(sp)                       # 4-byte Folded Spill
	sw	s5, 4(sp)                       # 4-byte Folded Spill
	mv	s2, a1
	mv	s3, a0
	call	strlen
	mv	s0, a0
	mv	a0, s2
	call	strlen
	mv	s4, a0
	add	s5, a0, s0
	addi	a0, s5, 1
	call	malloc
	mv	s1, a0
	mv	a1, s3
	mv	a2, s0
	call	memcpy
	add	a0, s1, s0
	mv	a1, s2
	mv	a2, s4
	call	memcpy
	add	a0, s1, s5
	sb	zero, 0(a0)
	mv	a0, s1
	lw	ra, 28(sp)                      # 4-byte Folded Reload
	lw	s0, 24(sp)                      # 4-byte Folded Reload
	lw	s1, 20(sp)                      # 4-byte Folded Reload
	lw	s2, 16(sp)                      # 4-byte Folded Reload
	lw	s3, 12(sp)                      # 4-byte Folded Reload
	lw	s4, 8(sp)                       # 4-byte Folded Reload
	lw	s5, 4(sp)                       # 4-byte Folded Reload
	addi	sp, sp, 32
	ret
.Lfunc_end11:
	.size	string.add, .Lfunc_end11-string.add
                                        # -- End function
	.globl	string.equal                    # -- Begin function string.equal
	.p2align	1
	.type	string.equal,@function
string.equal:                           # @string.equal
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	call	strcmp
	seqz	a0, a0
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end12:
	.size	string.equal, .Lfunc_end12-string.equal
                                        # -- End function
	.globl	string.notEqual                 # -- Begin function string.notEqual
	.p2align	1
	.type	string.notEqual,@function
string.notEqual:                        # @string.notEqual
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	call	strcmp
	snez	a0, a0
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end13:
	.size	string.notEqual, .Lfunc_end13-string.notEqual
                                        # -- End function
	.globl	string.less                     # -- Begin function string.less
	.p2align	1
	.type	string.less,@function
string.less:                            # @string.less
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	call	strcmp
	srli	a0, a0, 31
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end14:
	.size	string.less, .Lfunc_end14-string.less
                                        # -- End function
	.globl	string.lessOrEqual              # -- Begin function string.lessOrEqual
	.p2align	1
	.type	string.lessOrEqual,@function
string.lessOrEqual:                     # @string.lessOrEqual
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	call	strcmp
	slti	a0, a0, 1
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end15:
	.size	string.lessOrEqual, .Lfunc_end15-string.lessOrEqual
                                        # -- End function
	.globl	string.greater                  # -- Begin function string.greater
	.p2align	1
	.type	string.greater,@function
string.greater:                         # @string.greater
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	call	strcmp
	sgtz	a0, a0
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end16:
	.size	string.greater, .Lfunc_end16-string.greater
                                        # -- End function
	.globl	string.greaterOrEqual           # -- Begin function string.greaterOrEqual
	.p2align	1
	.type	string.greaterOrEqual,@function
string.greaterOrEqual:                  # @string.greaterOrEqual
# %bb.0:
	addi	sp, sp, -16
	sw	ra, 12(sp)                      # 4-byte Folded Spill
	call	strcmp
	not	a0, a0
	srli	a0, a0, 31
	lw	ra, 12(sp)                      # 4-byte Folded Reload
	addi	sp, sp, 16
	ret
.Lfunc_end17:
	.size	string.greaterOrEqual, .Lfunc_end17-string.greaterOrEqual
                                        # -- End function
	.globl	array.size                      # -- Begin function array.size
	.p2align	1
	.type	array.size,@function
array.size:                             # @array.size
# %bb.0:
	lw	a0, -4(a0)
	ret
.Lfunc_end18:
	.size	array.size, .Lfunc_end18-array.size
                                        # -- End function
	.globl	.malloc                         # -- Begin function .malloc
	.p2align	1
	.type	.malloc,@function
.malloc:                                # @.malloc
# %bb.0:
	tail	malloc
.Lfunc_end19:
	.size	.malloc, .Lfunc_end19-.malloc
                                        # -- End function
	.type	.L.str,@object                  # @.str
	.section	.rodata.str1.1,"aMS",@progbits,1
.L.str:
	.asciz	"%s"
	.size	.L.str, 3

	.type	.L.str.1,@object                # @.str.1
.L.str.1:
	.asciz	"%s\n"
	.size	.L.str.1, 4

	.type	.L.str.2,@object                # @.str.2
.L.str.2:
	.asciz	"%d"
	.size	.L.str.2, 3

	.type	.L.str.3,@object                # @.str.3
.L.str.3:
	.asciz	"%d\n"
	.size	.L.str.3, 4

	.ident	"Ubuntu clang version 14.0.0-1ubuntu1.1"
	.section	".note.GNU-stack","",@progbits
