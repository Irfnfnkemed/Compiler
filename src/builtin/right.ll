declare void @print(ptr)
declare void @println(ptr)
declare void @printInt(i32)
declare void @printlnInt(i32)
declare ptr @getString()
declare i32 @getInt()
declare ptr @toString(i32)
declare i32 @string.length(ptr)
declare ptr @string.substring(ptr,i32,i32)
declare i32 @string.parseInt(ptr)
declare i32 @string.ord(ptr,i32)
declare ptr @string.add(ptr,ptr)
declare i1 @string.equal(ptr,ptr)
declare i1 @string.notEqual(ptr,ptr)
declare i1 @string.less(ptr,ptr)
declare i1 @string.lessOrEqual(ptr,ptr)
declare i1 @string.greater(ptr,ptr)
declare i1 @greaterOrEqual(ptr,ptr)
declare i32 @array.size(ptr)
declare ptr @.malloc(i32)
@constString-0 = private unnamed_addr constant [14 x i8] c"no solution!\0A\00"

@N = global i32 0
@head = global i32 0
@targetx = global i32 0
@targety = global i32 0
@xlist = global ptr null
@ylist = global ptr null
@tail = global i32 0
@ok = global i32 0
@now = global i32 0
@step = global ptr null
@i = global i32 0
@j = global i32 0

define ptr @.newArray(i32 %_0) {
entry:
    %_1 = add i32 %_0, 1
    %_2 = call ptr @.malloc(i32 %_1)
    store i32 %_0, ptr %_2
    %_3 = getelementptr ptr, ptr %_2, i32 1
    ret ptr %_3
}

define void @.init-xlist() {
entry:
    %_0 = call ptr @.newArray(i32 12000)
    store ptr %_0, ptr @xlist
    ret void
}

define void @.init-ylist() {
entry:
    %_0 = call ptr @.newArray(i32 12000)
    store ptr %_0, ptr @ylist
    ret void
}

define void @origin(i32 %_0) {
entry:
    %N-37-12 = alloca i32
    store i32 %_0, ptr %N-37-12
    store i32 0, ptr @head
    store i32 0, ptr @tail
    %_1 = load i32, ptr %N-37-12
    %_2 = call ptr @.newArray(i32 %_1)
    store ptr %_2, ptr @step
    store i32 0, ptr @i
    br label %loopCondition-42-4
loopCondition-42-4:
    %_3 = load i32, ptr @i
    %_4 = load i32, ptr %N-37-12
    %_5 = icmp slt i32 %_3, %_4
    br i1 %_5, label %loopBody-42-4, label %loopTo-42-4
loopBody-42-4:
    %_6 = load i32, ptr %N-37-12
    %_7 = call ptr @.newArray(i32 %_6)
    %_8 = load i32, ptr @i
    %_9 = load ptr, ptr @step
    %_10 = getelementptr ptr, ptr %_9, i32 %_8
    store ptr %_7, ptr %_10
    store i32 0, ptr @j
    br label %loopCondition-44-8
loopCondition-44-8:
    %_11 = load i32, ptr @j
    %_12 = load i32, ptr %N-37-12
    %_13 = icmp slt i32 %_11, %_12
    br i1 %_13, label %loopBody-44-8, label %loopTo-44-8
loopBody-44-8:
    %_14 = load i32, ptr @j
    %_15 = load i32, ptr @i
    %_16 = load ptr, ptr @step
    %_17 = getelementptr ptr, ptr %_16, i32 %_15
    %_18 = load ptr, ptr %_17
    %_19 = getelementptr i32, ptr %_18, i32 %_14
    store i32 0, ptr %_19
    br label %loopStep-44-8
loopStep-44-8:
    %_20 = load i32, ptr @j
    %_21 = add i32 %_20, 1
    store i32 %_21, ptr @j
    br label %loopCondition-44-8
loopTo-44-8:
    br label %loopStep-42-4
loopStep-42-4:
    %_22 = load i32, ptr @i
    %_23 = add i32 %_22, 1
    store i32 %_23, ptr @i
    br label %loopCondition-42-4
loopTo-42-4:
    br label %returnLabel
returnLabel:
    ret void
}

define i1 @check(i32 %_0) {
entry:
    %.returnValue = alloca i1
    %a-49-11 = alloca i32
    store i1 false, ptr %.returnValue
    store i32 %_0, ptr %a-49-11
    %_1 = load i32, ptr %a-49-11
    %_2 = load i32, ptr @N
    %_3 = icmp slt i32 %_1, %_2
    br i1 %_3, label %andRhs-0, label %andTo-0
andRhs-0:
    %_4 = load i32, ptr %a-49-11
    %_5 = icmp sge i32 %_4, 0
    br label %andTo-0
andTo-0:
    %_6 = phi i1 [ false, %entry ], [ %_5, %andRhs-0 ]
    store i1 %_6, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_7 = load i1, ptr %.returnValue
    ret i1 %_7
}

define void @addList(i32 %_0, i32 %_1) {
entry:
    %x-53-13 = alloca i32
    %y-53-20 = alloca i32
    store i32 %_0, ptr %x-53-13
    store i32 %_1, ptr %y-53-20
    %_2 = load i32, ptr %x-53-13
    %_3 = call i1 @check(i32 %_2)
    br i1 %_3, label %andRhs-0, label %andTo-0
andRhs-0:
    %_4 = load i32, ptr %y-53-20
    %_5 = call i1 @check(i32 %_4)
    br label %andTo-0
andTo-0:
    %_6 = phi i1 [ false, %entry ], [ %_5, %andRhs-0 ]
    br i1 %_6, label %andRhs-1, label %andTo-1
andRhs-1:
    %_7 = load i32, ptr %y-53-20
    %_8 = load i32, ptr %x-53-13
    %_9 = load ptr, ptr @step
    %_10 = getelementptr ptr, ptr %_9, i32 %_8
    %_11 = load ptr, ptr %_10
    %_12 = getelementptr i32, ptr %_11, i32 %_7
    %_13 = load i32, ptr %_12
    %_14 = icmp eq i32 %_13, -1
    br label %andTo-1
andTo-1:
    %_15 = phi i1 [ false, %andTo-0 ], [ %_14, %andRhs-1 ]
    br i1 %_15, label %trueLabel-2, label %toLabel-2
trueLabel-2:
    %_16 = load i32, ptr @tail
    %_17 = add i32 %_16, 1
    store i32 %_17, ptr @tail
    %_18 = load i32, ptr %x-53-13
    %_19 = load i32, ptr @tail
    %_20 = load ptr, ptr @xlist
    %_21 = getelementptr i32, ptr %_20, i32 %_19
    store i32 %_18, ptr %_21
    %_22 = load i32, ptr %y-53-20
    %_23 = load i32, ptr @tail
    %_24 = load ptr, ptr @ylist
    %_25 = getelementptr i32, ptr %_24, i32 %_23
    store i32 %_22, ptr %_25
    %_26 = load i32, ptr @now
    %_27 = add i32 %_26, 1
    %_28 = load i32, ptr %y-53-20
    %_29 = load i32, ptr %x-53-13
    %_30 = load ptr, ptr @step
    %_31 = getelementptr ptr, ptr %_30, i32 %_29
    %_32 = load ptr, ptr %_31
    %_33 = getelementptr i32, ptr %_32, i32 %_28
    store i32 %_27, ptr %_33
    %_34 = load i32, ptr %x-53-13
    %_35 = load i32, ptr @targetx
    %_36 = icmp eq i32 %_34, %_35
    br i1 %_36, label %andRhs-3, label %andTo-3
andRhs-3:
    %_37 = load i32, ptr %y-53-20
    %_38 = load i32, ptr @targety
    %_39 = icmp eq i32 %_37, %_38
    br label %andTo-3
andTo-3:
    %_40 = phi i1 [ false, %trueLabel-2 ], [ %_39, %andRhs-3 ]
    br i1 %_40, label %trueLabel-4, label %toLabel-4
trueLabel-4:
    store i32 1, ptr @ok
    br label %toLabel-4
toLabel-4:
    br label %toLabel-2
toLabel-2:
    br label %returnLabel
returnLabel:
    ret void
}

define i32 @main() {
entry:
    call void @.init-xlist()
    call void @.init-ylist()
    %startx-20-4 = alloca i32
    %starty-21-4 = alloca i32
    %x-24-4 = alloca i32
    %y-25-4 = alloca i32
    %dx-31-6 = alloca ptr
    %dy-32-6 = alloca ptr
    %.returnValue = alloca i32
    store i32 0, ptr %startx-20-4
    store i32 0, ptr %starty-21-4
    store i32 0, ptr %x-24-4
    store i32 0, ptr %y-25-4
    %_0 = call ptr @.newArray(i32 8)
    store ptr %_0, ptr %dx-31-6
    %_1 = call ptr @.newArray(i32 9)
    store ptr %_1, ptr %dy-32-6
    store i32 0, ptr %.returnValue
    call void @origin(i32 106)
    %_2 = call i32 @getInt()
    store i32 %_2, ptr @N
    %_3 = load i32, ptr @N
    %_4 = sub i32 %_3, 1
    store i32 %_4, ptr @targety
    %_5 = load i32, ptr @targety
    store i32 %_5, ptr @targetx
    store i32 0, ptr @i
    br label %loopCondition-67-4
loopCondition-67-4:
    %_6 = load i32, ptr @i
    %_7 = load i32, ptr @N
    %_8 = icmp slt i32 %_6, %_7
    br i1 %_8, label %loopBody-67-4, label %loopTo-67-4
loopBody-67-4:
    store i32 0, ptr @j
    br label %loopCondition-68-8
loopCondition-68-8:
    %_9 = load i32, ptr @j
    %_10 = load i32, ptr @N
    %_11 = icmp slt i32 %_9, %_10
    br i1 %_11, label %loopBody-68-8, label %loopTo-68-8
loopBody-68-8:
    %_12 = load i32, ptr @j
    %_13 = load i32, ptr @i
    %_14 = load ptr, ptr @step
    %_15 = getelementptr ptr, ptr %_14, i32 %_13
    %_16 = load ptr, ptr %_15
    %_17 = getelementptr i32, ptr %_16, i32 %_12
    store i32 -1, ptr %_17
    br label %loopStep-68-8
loopStep-68-8:
    %_18 = load i32, ptr @j
    %_19 = add i32 %_18, 1
    store i32 %_19, ptr @j
    br label %loopCondition-68-8
loopTo-68-8:
    br label %loopStep-67-4
loopStep-67-4:
    %_20 = load i32, ptr @i
    %_21 = add i32 %_20, 1
    store i32 %_21, ptr @i
    br label %loopCondition-67-4
loopTo-67-4:
    %_22 = load ptr, ptr %dx-31-6
    %_23 = getelementptr i32, ptr %_22, i32 0
    store i32 -2, ptr %_23
    %_24 = load ptr, ptr %dy-32-6
    %_25 = getelementptr i32, ptr %_24, i32 0
    store i32 -1, ptr %_25
    %_26 = load ptr, ptr %dx-31-6
    %_27 = getelementptr i32, ptr %_26, i32 1
    store i32 -2, ptr %_27
    %_28 = load ptr, ptr %dy-32-6
    %_29 = getelementptr i32, ptr %_28, i32 1
    store i32 1, ptr %_29
    %_30 = load ptr, ptr %dx-31-6
    %_31 = getelementptr i32, ptr %_30, i32 2
    store i32 2, ptr %_31
    %_32 = load ptr, ptr %dy-32-6
    %_33 = getelementptr i32, ptr %_32, i32 2
    store i32 -1, ptr %_33
    %_34 = load ptr, ptr %dx-31-6
    %_35 = getelementptr i32, ptr %_34, i32 3
    store i32 2, ptr %_35
    %_36 = load ptr, ptr %dy-32-6
    %_37 = getelementptr i32, ptr %_36, i32 3
    store i32 1, ptr %_37
    %_38 = load ptr, ptr %dx-31-6
    %_39 = getelementptr i32, ptr %_38, i32 4
    store i32 -1, ptr %_39
    %_40 = load ptr, ptr %dy-32-6
    %_41 = getelementptr i32, ptr %_40, i32 4
    store i32 -2, ptr %_41
    %_42 = load ptr, ptr %dx-31-6
    %_43 = getelementptr i32, ptr %_42, i32 5
    store i32 -1, ptr %_43
    %_44 = load ptr, ptr %dy-32-6
    %_45 = getelementptr i32, ptr %_44, i32 5
    store i32 2, ptr %_45
    %_46 = load ptr, ptr %dx-31-6
    %_47 = getelementptr i32, ptr %_46, i32 6
    store i32 1, ptr %_47
    %_48 = load ptr, ptr %dy-32-6
    %_49 = getelementptr i32, ptr %_48, i32 6
    store i32 -2, ptr %_49
    %_50 = load ptr, ptr %dx-31-6
    %_51 = getelementptr i32, ptr %_50, i32 7
    store i32 1, ptr %_51
    %_52 = load ptr, ptr %dy-32-6
    %_53 = getelementptr i32, ptr %_52, i32 7
    store i32 2, ptr %_53
    br label %loopCondition-78-4
loopCondition-78-4:
    %_54 = load i32, ptr @head
    %_55 = load i32, ptr @tail
    %_56 = icmp sle i32 %_54, %_55
    br i1 %_56, label %loopBody-78-4, label %loopTo-78-4
loopBody-78-4:
    %_57 = load i32, ptr @head
    %_58 = load ptr, ptr @xlist
    %_59 = getelementptr i32, ptr %_58, i32 %_57
    %_60 = load i32, ptr %_59
    store i32 %_60, ptr %x-24-4
    %_61 = load i32, ptr @head
    %_62 = load ptr, ptr @ylist
    %_63 = getelementptr i32, ptr %_62, i32 %_61
    %_64 = load i32, ptr %_63
    store i32 %_64, ptr %y-25-4
    %_65 = load i32, ptr %y-25-4
    %_66 = load i32, ptr %x-24-4
    %_67 = load ptr, ptr @step
    %_68 = getelementptr ptr, ptr %_67, i32 %_66
    %_69 = load ptr, ptr %_68
    %_70 = getelementptr i32, ptr %_69, i32 %_65
    %_71 = load i32, ptr %_70
    store i32 %_71, ptr @now
    store i32 0, ptr @j
    br label %loopCondition-82-8
loopCondition-82-8:
    %_72 = load i32, ptr @j
    %_73 = icmp slt i32 %_72, 8
    br i1 %_73, label %loopBody-82-8, label %loopTo-82-8
loopBody-82-8:
    %_74 = load i32, ptr %x-24-4
    %_75 = load i32, ptr @j
    %_76 = load ptr, ptr %dx-31-6
    %_77 = getelementptr i32, ptr %_76, i32 %_75
    %_78 = load i32, ptr %_77
    %_79 = add i32 %_74, %_78
    %_80 = load i32, ptr %y-25-4
    %_81 = load i32, ptr @j
    %_82 = load ptr, ptr %dy-32-6
    %_83 = getelementptr i32, ptr %_82, i32 %_81
    %_84 = load i32, ptr %_83
    %_85 = add i32 %_80, %_84
    call void @addList(i32 %_79, i32 %_85)
    br label %loopStep-82-8
loopStep-82-8:
    %_86 = load i32, ptr @j
    %_87 = add i32 %_86, 1
    store i32 %_87, ptr @j
    br label %loopCondition-82-8
loopTo-82-8:
    %_88 = load i32, ptr @ok
    %_89 = icmp eq i32 %_88, 1
    br i1 %_89, label %trueLabel-0, label %toLabel-0
trueLabel-0:
    br label %loopTo-78-4
toLabel-0:
    %_90 = load i32, ptr @head
    %_91 = add i32 %_90, 1
    store i32 %_91, ptr @head
    br label %loopStep-78-4
loopStep-78-4:
    br label %loopCondition-78-4
loopTo-78-4:
    %_92 = load i32, ptr @ok
    %_93 = icmp eq i32 %_92, 1
    br i1 %_93, label %trueLabel-1, label %falseLabel-1
trueLabel-1:
    %_94 = load i32, ptr @targety
    %_95 = load i32, ptr @targetx
    %_96 = load ptr, ptr @step
    %_97 = getelementptr ptr, ptr %_96, i32 %_95
    %_98 = load ptr, ptr %_97
    %_99 = getelementptr i32, ptr %_98, i32 %_94
    %_100 = load i32, ptr %_99
    %_101 = call ptr @toString(i32 %_100)
    call void @println(ptr %_101)
    br label %toLabel-1
falseLabel-1:
    call void @print(ptr @constString-0)
    br label %toLabel-1
toLabel-1:
    store i32 0, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_102 = load i32, ptr %.returnValue
    ret i32 %_102
}
