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

define ptr @.newArray(i32 %_0) {
entry:
    %_1 = add i32 %_0, 1
    %_2 = call ptr @.malloc(i32 %_1)
    store i32 %_0, ptr %_2
    %_3 = getelementptr ptr, ptr %_2, i32 1
    ret ptr %_3
}

define i32 @main() {
entry:
    %a-1-7 = alloca ptr
    %.returnValue = alloca i32
    %x-4-8 = alloca i32
    %y-4-22 = alloca i32
    %z-4-36 = alloca i32
    %i-5-12 = alloca i32
    %j-6-16 = alloca i32
    %_0 = call ptr @.newArray(i32 100)
    br label %newArrayCondition-0
newArrayCondition-0:
    %_1 = phi i32 [ 0, %entry ], [ %_5, %newArrayBody-0 ]
    %_2 = icmp slt i32 %_1, 100
    br i1 %_2, label %newArrayBody-0, label %newArray-To-0
newArrayBody-0:
    %_3 = call ptr @.newArray(i32 100)
    %_4 = getelementptr ptr, ptr %_0, i32 %_1
    store ptr %_3, ptr %_4
    %_5 = add i32 %_1, 1
    br label %newArrayCondition-0
newArray-To-0:
    store ptr %_0, ptr %a-1-7
    store i32 0, ptr %.returnValue
    %_6 = call i32 @getInt()
    store i32 %_6, ptr %x-4-8
    %_7 = call i32 @getInt()
    store i32 %_7, ptr %y-4-22
    %_8 = call i32 @getInt()
    store i32 %_8, ptr %z-4-36
    store i32 0, ptr %i-5-12
    br label %loopCondition-5-4
loopCondition-5-4:
    %_9 = load i32, ptr %i-5-12
    %_10 = icmp slt i32 %_9, 100
    br i1 %_10, label %loopBody-5-4, label %loopTo-5-4
loopBody-5-4:
    store i32 0, ptr %j-6-16
    br label %loopCondition-6-8
loopCondition-6-8:
    %_11 = load i32, ptr %j-6-16
    %_12 = icmp slt i32 %_11, 100
    br i1 %_12, label %loopBody-6-8, label %loopTo-6-8
loopBody-6-8:
    %_13 = load i32, ptr %z-4-36
    %_14 = load i32, ptr %y-4-22
    %_15 = add i32 %_13, %_14
    %_16 = load i32, ptr %x-4-8
    %_17 = add i32 %_15, %_16
    %_18 = load i32, ptr %j-6-16
    %_19 = load i32, ptr %i-5-12
    %_20 = load ptr, ptr %a-1-7
    %_21 = getelementptr ptr, ptr %_20, i32 %_19
    %_22 = load ptr, ptr %_21
    %_23 = getelementptr i32, ptr %_22, i32 %_18
    store i32 %_17, ptr %_23
    br label %loopStep-6-8
loopStep-6-8:
    %_24 = load i32, ptr %j-6-16
    %_25 = add i32 %_24, 1
    store i32 %_25, ptr %j-6-16
    br label %loopCondition-6-8
loopTo-6-8:
    br label %loopStep-5-4
loopStep-5-4:
    %_26 = load i32, ptr %i-5-12
    %_27 = add i32 %_26, 1
    store i32 %_27, ptr %i-5-12
    br label %loopCondition-5-4
loopTo-5-4:
    br label %returnLabel
returnLabel:
    %_28 = load i32, ptr %.returnValue
    ret i32 %_28
}
