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
@constString-2 = private unnamed_addr constant [1 x i8] c"\00"
@constString-1 = private unnamed_addr constant [2 x i8] c" \00"
@constString-0 = private unnamed_addr constant [3 x i8] c"-1\00"

%class-Edge = type { i32, i32, i32 }
%class-EdgeList = type { ptr, ptr, ptr, i32 }
%class-Array_Node = type { ptr, i32 }
%class-Heap_Node = type { ptr }
%class-Node = type { i32, i32 }
@n = global i32 0
@m = global i32 0
@g = global ptr null
@INF = global i32 10000000

define ptr @.newArray(i32 %_0) {
entry:
    %_1 = add i32 %_0, 1
    %_2 = call ptr @.malloc(i32 %_1)
    store i32 %_0, ptr %_2
    %_3 = getelementptr ptr, ptr %_2, i32 1
    ret ptr %_3
}

define ptr @.init-class-Edge() {
entry:
    %this = call ptr @.malloc(i32 3)
    %_0 = getelementptr %class-Edge, ptr %this, i32 0, i32 0
    store i32 0, ptr %_0
    %_1 = getelementptr %class-Edge, ptr %this, i32 0, i32 1
    store i32 0, ptr %_1
    %_2 = getelementptr %class-Edge, ptr %this, i32 0, i32 2
    store i32 0, ptr %_2
    br label %returnLabel
returnLabel:
    ret ptr %this
}

define ptr @.init-class-EdgeList() {
entry:
    %this = call ptr @.malloc(i32 4)
    %_0 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 0
    store ptr null, ptr %_0
    %_1 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 1
    store ptr null, ptr %_1
    %_2 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 2
    store ptr null, ptr %_2
    %_3 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 3
    store i32 0, ptr %_3
    br label %returnLabel
returnLabel:
    ret ptr %this
}

define void @EdgeList.init(ptr %this, i32 %_0, i32 %_1) {
entry:
    %n-2090-12 = alloca i32
    %m-2090-19 = alloca i32
    %i-2094-8 = alloca i32
    store i32 %_0, ptr %n-2090-12
    store i32 %_1, ptr %m-2090-19
    %_2 = load i32, ptr %m-2090-19
    %_3 = call ptr @.newArray(i32 %_2)
    %_4 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 0
    store ptr %_3, ptr %_4
    %_5 = load i32, ptr %m-2090-19
    %_6 = call ptr @.newArray(i32 %_5)
    %_7 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 1
    store ptr %_6, ptr %_7
    %_8 = load i32, ptr %n-2090-12
    %_9 = call ptr @.newArray(i32 %_8)
    %_10 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 2
    store ptr %_9, ptr %_10
    store i32 0, ptr %i-2094-8
    store i32 0, ptr %i-2094-8
    br label %loopCondition-2095-4
loopCondition-2095-4:
    %_11 = load i32, ptr %i-2094-8
    %_12 = load i32, ptr %m-2090-19
    %_13 = icmp slt i32 %_11, %_12
    br i1 %_13, label %loopBody-2095-4, label %loopTo-2095-4
loopBody-2095-4:
    %_14 = load i32, ptr %i-2094-8
    %_15 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 1
    %_16 = load ptr, ptr %_15
    %_17 = getelementptr i32, ptr %_16, i32 %_14
    store i32 -1, ptr %_17
    br label %loopStep-2095-4
loopStep-2095-4:
    %_18 = load i32, ptr %i-2094-8
    %_19 = add i32 %_18, 1
    store i32 %_19, ptr %i-2094-8
    br label %loopCondition-2095-4
loopTo-2095-4:
    store i32 0, ptr %i-2094-8
    br label %loopCondition-2097-4
loopCondition-2097-4:
    %_20 = load i32, ptr %i-2094-8
    %_21 = load i32, ptr %n-2090-12
    %_22 = icmp slt i32 %_20, %_21
    br i1 %_22, label %loopBody-2097-4, label %loopTo-2097-4
loopBody-2097-4:
    %_23 = load i32, ptr %i-2094-8
    %_24 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 2
    %_25 = load ptr, ptr %_24
    %_26 = getelementptr i32, ptr %_25, i32 %_23
    store i32 -1, ptr %_26
    br label %loopStep-2097-4
loopStep-2097-4:
    %_27 = load i32, ptr %i-2094-8
    %_28 = add i32 %_27, 1
    store i32 %_28, ptr %i-2094-8
    br label %loopCondition-2097-4
loopTo-2097-4:
    %_29 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 3
    store i32 0, ptr %_29
    br label %returnLabel
returnLabel:
    ret void
}

define void @EdgeList.addEdge(ptr %this, i32 %_0, i32 %_1, i32 %_2) {
entry:
    %u-2102-15 = alloca i32
    %v-2102-22 = alloca i32
    %w-2102-29 = alloca i32
    %e-2103-9 = alloca ptr
    store i32 %_0, ptr %u-2102-15
    store i32 %_1, ptr %v-2102-22
    store i32 %_2, ptr %w-2102-29
    %_3 = call ptr @.init-class-Edge()
    store ptr %_3, ptr %e-2103-9
    %_4 = load i32, ptr %u-2102-15
    %_5 = load ptr, ptr %e-2103-9
    %_6 = getelementptr %class-Edge, ptr %_5, i32 0, i32 0
    store i32 %_4, ptr %_6
    %_7 = load i32, ptr %v-2102-22
    %_8 = load ptr, ptr %e-2103-9
    %_9 = getelementptr %class-Edge, ptr %_8, i32 0, i32 1
    store i32 %_7, ptr %_9
    %_10 = load i32, ptr %w-2102-29
    %_11 = load ptr, ptr %e-2103-9
    %_12 = getelementptr %class-Edge, ptr %_11, i32 0, i32 2
    store i32 %_10, ptr %_12
    %_13 = load ptr, ptr %e-2103-9
    %_14 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 3
    %_15 = load i32, ptr %_14
    %_16 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 0
    %_17 = load ptr, ptr %_16
    %_18 = getelementptr ptr, ptr %_17, i32 %_15
    store ptr %_13, ptr %_18
    %_19 = load i32, ptr %u-2102-15
    %_20 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 2
    %_21 = load ptr, ptr %_20
    %_22 = getelementptr i32, ptr %_21, i32 %_19
    %_23 = load i32, ptr %_22
    %_24 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 3
    %_25 = load i32, ptr %_24
    %_26 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 1
    %_27 = load ptr, ptr %_26
    %_28 = getelementptr i32, ptr %_27, i32 %_25
    store i32 %_23, ptr %_28
    %_29 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 3
    %_30 = load i32, ptr %_29
    %_31 = load i32, ptr %u-2102-15
    %_32 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 2
    %_33 = load ptr, ptr %_32
    %_34 = getelementptr i32, ptr %_33, i32 %_31
    store i32 %_30, ptr %_34
    %_35 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 3
    %_36 = load i32, ptr %_35
    %_37 = add i32 %_36, 1
    store i32 %_37, ptr %_35
    br label %returnLabel
returnLabel:
    ret void
}

define i32 @EdgeList.nVertices(ptr %this) {
entry:
    %.returnValue = alloca i32
    store i32 0, ptr %.returnValue
    %_0 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 2
    %_1 = load ptr, ptr %_0
    %_2 = call i32 @array.size(ptr %_1)
    store i32 %_2, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_3 = load i32, ptr %.returnValue
    ret i32 %_3
}

define i32 @EdgeList.nEdges(ptr %this) {
entry:
    %.returnValue = alloca i32
    store i32 0, ptr %.returnValue
    %_0 = getelementptr %class-EdgeList, ptr %this, i32 0, i32 0
    %_1 = load ptr, ptr %_0
    %_2 = call i32 @array.size(ptr %_1)
    store i32 %_2, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_3 = load i32, ptr %.returnValue
    ret i32 %_3
}

define ptr @.init-class-Array_Node() {
entry:
    %this = call ptr @.malloc(i32 2)
    %_0 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    store ptr null, ptr %_0
    %_1 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    store i32 0, ptr %_1
    %_2 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    store i32 0, ptr %_2
    %_3 = call ptr @.newArray(i32 16)
    %_4 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    store ptr %_3, ptr %_4
    br label %returnLabel
returnLabel:
    ret ptr %this
}

define void @Array_Node.push_back(ptr %this, ptr %_0) {
entry:
    %v-2134-17 = alloca ptr
    store ptr %_0, ptr %v-2134-17
    %_1 = call i32 @Array_Node.size(ptr %this)
    %_2 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_3 = load ptr, ptr %_2
    %_4 = call i32 @array.size(ptr %_3)
    %_5 = icmp eq i32 %_1, %_4
    br i1 %_5, label %trueLabel-0, label %toLabel-0
trueLabel-0:
    call void @Array_Node.doubleStorage(ptr %this)
    br label %toLabel-0
toLabel-0:
    %_6 = load ptr, ptr %v-2134-17
    %_7 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_8 = load i32, ptr %_7
    %_9 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_10 = load ptr, ptr %_9
    %_11 = getelementptr ptr, ptr %_10, i32 %_8
    store ptr %_6, ptr %_11
    %_12 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_13 = load i32, ptr %_12
    %_14 = add i32 %_13, 1
    store i32 %_14, ptr %_12
    br label %returnLabel
returnLabel:
    ret void
}

define ptr @Array_Node.pop_back(ptr %this) {
entry:
    %.returnValue = alloca ptr
    store ptr null, ptr %.returnValue
    %_0 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_1 = load i32, ptr %_0
    %_2 = sub i32 %_1, 1
    store i32 %_2, ptr %_0
    %_3 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_4 = load i32, ptr %_3
    %_5 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_6 = load ptr, ptr %_5
    %_7 = getelementptr ptr, ptr %_6, i32 %_4
    %_8 = load ptr, ptr %_7
    store ptr %_8, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_9 = load ptr, ptr %.returnValue
    ret ptr %_9
}

define ptr @Array_Node.back(ptr %this) {
entry:
    %.returnValue = alloca ptr
    store ptr null, ptr %.returnValue
    %_0 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_1 = load i32, ptr %_0
    %_2 = sub i32 %_1, 1
    %_3 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_4 = load ptr, ptr %_3
    %_5 = getelementptr ptr, ptr %_4, i32 %_2
    %_6 = load ptr, ptr %_5
    store ptr %_6, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_7 = load ptr, ptr %.returnValue
    ret ptr %_7
}

define ptr @Array_Node.front(ptr %this) {
entry:
    %.returnValue = alloca ptr
    store ptr null, ptr %.returnValue
    %_0 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_1 = load ptr, ptr %_0
    %_2 = getelementptr ptr, ptr %_1, i32 0
    %_3 = load ptr, ptr %_2
    store ptr %_3, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_4 = load ptr, ptr %.returnValue
    ret ptr %_4
}

define i32 @Array_Node.size(ptr %this) {
entry:
    %.returnValue = alloca i32
    store i32 0, ptr %.returnValue
    %_0 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_1 = load i32, ptr %_0
    store i32 %_1, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_2 = load i32, ptr %.returnValue
    ret i32 %_2
}

define void @Array_Node.resize(ptr %this, i32 %_0) {
entry:
    %newSize-2159-14 = alloca i32
    store i32 %_0, ptr %newSize-2159-14
    br label %loopCondition-2160-4
loopCondition-2160-4:
    %_1 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_2 = load ptr, ptr %_1
    %_3 = call i32 @array.size(ptr %_2)
    %_4 = load i32, ptr %newSize-2159-14
    %_5 = icmp slt i32 %_3, %_4
    br i1 %_5, label %loopBody-2160-4, label %loopTo-2160-4
loopBody-2160-4:
    call void @Array_Node.doubleStorage(ptr %this)
    br label %loopStep-2160-4
loopStep-2160-4:
    br label %loopCondition-2160-4
loopTo-2160-4:
    %_6 = load i32, ptr %newSize-2159-14
    %_7 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    store i32 %_6, ptr %_7
    br label %returnLabel
returnLabel:
    ret void
}

define ptr @Array_Node.get(ptr %this, i32 %_0) {
entry:
    %.returnValue = alloca ptr
    %i-2165-11 = alloca i32
    store ptr null, ptr %.returnValue
    store i32 %_0, ptr %i-2165-11
    %_1 = load i32, ptr %i-2165-11
    %_2 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_3 = load ptr, ptr %_2
    %_4 = getelementptr ptr, ptr %_3, i32 %_1
    %_5 = load ptr, ptr %_4
    store ptr %_5, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_6 = load ptr, ptr %.returnValue
    ret ptr %_6
}

define void @Array_Node.set(ptr %this, i32 %_0, ptr %_1) {
entry:
    %i-2169-11 = alloca i32
    %v-2169-18 = alloca ptr
    store i32 %_0, ptr %i-2169-11
    store ptr %_1, ptr %v-2169-18
    %_2 = load ptr, ptr %v-2169-18
    %_3 = load i32, ptr %i-2169-11
    %_4 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_5 = load ptr, ptr %_4
    %_6 = getelementptr ptr, ptr %_5, i32 %_3
    store ptr %_2, ptr %_6
    br label %returnLabel
returnLabel:
    ret void
}

define void @Array_Node.swap(ptr %this, i32 %_0, i32 %_1) {
entry:
    %i-2173-12 = alloca i32
    %j-2173-19 = alloca i32
    %t-2174-9 = alloca ptr
    store i32 %_0, ptr %i-2173-12
    store i32 %_1, ptr %j-2173-19
    %_2 = load i32, ptr %i-2173-12
    %_3 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_4 = load ptr, ptr %_3
    %_5 = getelementptr ptr, ptr %_4, i32 %_2
    %_6 = load ptr, ptr %_5
    store ptr %_6, ptr %t-2174-9
    %_7 = load i32, ptr %j-2173-19
    %_8 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_9 = load ptr, ptr %_8
    %_10 = getelementptr ptr, ptr %_9, i32 %_7
    %_11 = load ptr, ptr %_10
    %_12 = load i32, ptr %i-2173-12
    %_13 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_14 = load ptr, ptr %_13
    %_15 = getelementptr ptr, ptr %_14, i32 %_12
    store ptr %_11, ptr %_15
    %_16 = load ptr, ptr %t-2174-9
    %_17 = load i32, ptr %j-2173-19
    %_18 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_19 = load ptr, ptr %_18
    %_20 = getelementptr ptr, ptr %_19, i32 %_17
    store ptr %_16, ptr %_20
    br label %returnLabel
returnLabel:
    ret void
}

define void @Array_Node.doubleStorage(ptr %this) {
entry:
    %copy-2182-11 = alloca ptr
    %szCopy-2183-8 = alloca i32
    %_0 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_1 = load ptr, ptr %_0
    store ptr %_1, ptr %copy-2182-11
    %_2 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_3 = load i32, ptr %_2
    store i32 %_3, ptr %szCopy-2183-8
    %_4 = load ptr, ptr %copy-2182-11
    %_5 = call i32 @array.size(ptr %_4)
    %_6 = mul i32 %_5, 2
    %_7 = call ptr @.newArray(i32 %_6)
    %_8 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    store ptr %_7, ptr %_8
    %_9 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    store i32 0, ptr %_9
    br label %loopCondition-2188-4
loopCondition-2188-4:
    %_10 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_11 = load i32, ptr %_10
    %_12 = load i32, ptr %szCopy-2183-8
    %_13 = icmp ne i32 %_11, %_12
    br i1 %_13, label %loopBody-2188-4, label %loopTo-2188-4
loopBody-2188-4:
    %_14 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_15 = load i32, ptr %_14
    %_16 = load ptr, ptr %copy-2182-11
    %_17 = getelementptr ptr, ptr %_16, i32 %_15
    %_18 = load ptr, ptr %_17
    %_19 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_20 = load i32, ptr %_19
    %_21 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 0
    %_22 = load ptr, ptr %_21
    %_23 = getelementptr ptr, ptr %_22, i32 %_20
    store ptr %_18, ptr %_23
    br label %loopStep-2188-4
loopStep-2188-4:
    %_24 = getelementptr %class-Array_Node, ptr %this, i32 0, i32 1
    %_25 = load i32, ptr %_24
    %_26 = add i32 %_25, 1
    store i32 %_26, ptr %_24
    br label %loopCondition-2188-4
loopTo-2188-4:
    br label %returnLabel
returnLabel:
    ret void
}

define ptr @.init-class-Heap_Node() {
entry:
    %this = call ptr @.malloc(i32 1)
    %_0 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    store ptr null, ptr %_0
    %_1 = call ptr @.init-class-Array_Node()
    %_2 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    store ptr %_1, ptr %_2
    br label %returnLabel
returnLabel:
    ret ptr %this
}

define void @Heap_Node.push(ptr %this, ptr %_0) {
entry:
    %v-2204-12 = alloca ptr
    %x-2206-8 = alloca i32
    %p-2208-10 = alloca i32
    store ptr %_0, ptr %v-2204-12
    %_1 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_2 = load ptr, ptr %_1
    %_3 = load ptr, ptr %v-2204-12
    call void @Array_Node.push_back(ptr %_2, ptr %_3)
    %_4 = call i32 @Heap_Node.size(ptr %this)
    %_5 = sub i32 %_4, 1
    store i32 %_5, ptr %x-2206-8
    br label %loopCondition-2207-4
loopCondition-2207-4:
    %_6 = load i32, ptr %x-2206-8
    %_7 = icmp sgt i32 %_6, 0
    br i1 %_7, label %loopBody-2207-4, label %loopTo-2207-4
loopBody-2207-4:
    %_8 = load i32, ptr %x-2206-8
    %_9 = call i32 @Heap_Node.pnt(ptr %this, i32 %_8)
    store i32 %_9, ptr %p-2208-10
    %_10 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_11 = load ptr, ptr %_10
    %_12 = load i32, ptr %p-2208-10
    %_13 = call ptr @Array_Node.get(ptr %_11, i32 %_12)
    %_14 = call i32 @Node.key_(ptr %_13)
    %_15 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_16 = load ptr, ptr %_15
    %_17 = load i32, ptr %x-2206-8
    %_18 = call ptr @Array_Node.get(ptr %_16, i32 %_17)
    %_19 = call i32 @Node.key_(ptr %_18)
    %_20 = icmp sge i32 %_14, %_19
    br i1 %_20, label %trueLabel-0, label %toLabel-0
trueLabel-0:
    br label %loopTo-2207-4
toLabel-0:
    %_21 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_22 = load ptr, ptr %_21
    %_23 = load i32, ptr %p-2208-10
    %_24 = load i32, ptr %x-2206-8
    call void @Array_Node.swap(ptr %_22, i32 %_23, i32 %_24)
    %_25 = load i32, ptr %p-2208-10
    store i32 %_25, ptr %x-2206-8
    br label %loopStep-2207-4
loopStep-2207-4:
    br label %loopCondition-2207-4
loopTo-2207-4:
    br label %returnLabel
returnLabel:
    ret void
}

define ptr @Heap_Node.pop(ptr %this) {
entry:
    %.returnValue = alloca ptr
    %res-2217-9 = alloca ptr
    store ptr null, ptr %.returnValue
    %_0 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_1 = load ptr, ptr %_0
    %_2 = call ptr @Array_Node.front(ptr %_1)
    store ptr %_2, ptr %res-2217-9
    %_3 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_4 = load ptr, ptr %_3
    %_5 = call i32 @Heap_Node.size(ptr %this)
    %_6 = sub i32 %_5, 1
    call void @Array_Node.swap(ptr %_4, i32 0, i32 %_6)
    %_7 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_8 = load ptr, ptr %_7
    %_9 = call ptr @Array_Node.pop_back(ptr %_8)
    call void @Heap_Node.maxHeapify(ptr %this, i32 0)
    %_10 = load ptr, ptr %res-2217-9
    store ptr %_10, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_11 = load ptr, ptr %.returnValue
    ret ptr %_11
}

define ptr @Heap_Node.top(ptr %this) {
entry:
    %.returnValue = alloca ptr
    store ptr null, ptr %.returnValue
    %_0 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_1 = load ptr, ptr %_0
    %_2 = call ptr @Array_Node.front(ptr %_1)
    store ptr %_2, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_3 = load ptr, ptr %.returnValue
    ret ptr %_3
}

define i32 @Heap_Node.size(ptr %this) {
entry:
    %.returnValue = alloca i32
    store i32 0, ptr %.returnValue
    %_0 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_1 = load ptr, ptr %_0
    %_2 = call i32 @Array_Node.size(ptr %_1)
    store i32 %_2, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_3 = load i32, ptr %.returnValue
    ret i32 %_3
}

define i32 @Heap_Node.lchild(ptr %this, i32 %_0) {
entry:
    %.returnValue = alloca i32
    %x-2233-13 = alloca i32
    store i32 0, ptr %.returnValue
    store i32 %_0, ptr %x-2233-13
    %_1 = load i32, ptr %x-2233-13
    %_2 = mul i32 %_1, 2
    %_3 = add i32 %_2, 1
    store i32 %_3, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_4 = load i32, ptr %.returnValue
    ret i32 %_4
}

define i32 @Heap_Node.rchild(ptr %this, i32 %_0) {
entry:
    %.returnValue = alloca i32
    %x-2237-13 = alloca i32
    store i32 0, ptr %.returnValue
    store i32 %_0, ptr %x-2237-13
    %_1 = load i32, ptr %x-2237-13
    %_2 = mul i32 %_1, 2
    %_3 = add i32 %_2, 2
    store i32 %_3, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_4 = load i32, ptr %.returnValue
    ret i32 %_4
}

define i32 @Heap_Node.pnt(ptr %this, i32 %_0) {
entry:
    %.returnValue = alloca i32
    %x-2241-10 = alloca i32
    store i32 0, ptr %.returnValue
    store i32 %_0, ptr %x-2241-10
    %_1 = load i32, ptr %x-2241-10
    %_2 = sub i32 %_1, 1
    %_3 = sdiv i32 %_2, 2
    store i32 %_3, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_4 = load i32, ptr %.returnValue
    ret i32 %_4
}

define void @Heap_Node.maxHeapify(ptr %this, i32 %_0) {
entry:
    %x-2245-18 = alloca i32
    %l-2246-8 = alloca i32
    %r-2247-8 = alloca i32
    %largest-2248-8 = alloca i32
    store i32 %_0, ptr %x-2245-18
    %_1 = load i32, ptr %x-2245-18
    %_2 = call i32 @Heap_Node.lchild(ptr %this, i32 %_1)
    store i32 %_2, ptr %l-2246-8
    %_3 = load i32, ptr %x-2245-18
    %_4 = call i32 @Heap_Node.rchild(ptr %this, i32 %_3)
    store i32 %_4, ptr %r-2247-8
    %_5 = load i32, ptr %x-2245-18
    store i32 %_5, ptr %largest-2248-8
    %_6 = load i32, ptr %l-2246-8
    %_7 = call i32 @Heap_Node.size(ptr %this)
    %_8 = icmp slt i32 %_6, %_7
    br i1 %_8, label %andRhs-0, label %andTo-0
andRhs-0:
    %_9 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_10 = load ptr, ptr %_9
    %_11 = load i32, ptr %l-2246-8
    %_12 = call ptr @Array_Node.get(ptr %_10, i32 %_11)
    %_13 = call i32 @Node.key_(ptr %_12)
    %_14 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_15 = load ptr, ptr %_14
    %_16 = load i32, ptr %largest-2248-8
    %_17 = call ptr @Array_Node.get(ptr %_15, i32 %_16)
    %_18 = call i32 @Node.key_(ptr %_17)
    %_19 = icmp sgt i32 %_13, %_18
    br label %andTo-0
andTo-0:
    %_20 = phi i1 [ false, %entry ], [ %_19, %andRhs-0 ]
    br i1 %_20, label %trueLabel-1, label %toLabel-1
trueLabel-1:
    %_21 = load i32, ptr %l-2246-8
    store i32 %_21, ptr %largest-2248-8
    br label %toLabel-1
toLabel-1:
    %_22 = load i32, ptr %r-2247-8
    %_23 = call i32 @Heap_Node.size(ptr %this)
    %_24 = icmp slt i32 %_22, %_23
    br i1 %_24, label %andRhs-2, label %andTo-2
andRhs-2:
    %_25 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_26 = load ptr, ptr %_25
    %_27 = load i32, ptr %r-2247-8
    %_28 = call ptr @Array_Node.get(ptr %_26, i32 %_27)
    %_29 = call i32 @Node.key_(ptr %_28)
    %_30 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_31 = load ptr, ptr %_30
    %_32 = load i32, ptr %largest-2248-8
    %_33 = call ptr @Array_Node.get(ptr %_31, i32 %_32)
    %_34 = call i32 @Node.key_(ptr %_33)
    %_35 = icmp sgt i32 %_29, %_34
    br label %andTo-2
andTo-2:
    %_36 = phi i1 [ false, %toLabel-1 ], [ %_35, %andRhs-2 ]
    br i1 %_36, label %trueLabel-3, label %toLabel-3
trueLabel-3:
    %_37 = load i32, ptr %r-2247-8
    store i32 %_37, ptr %largest-2248-8
    br label %toLabel-3
toLabel-3:
    %_38 = load i32, ptr %largest-2248-8
    %_39 = load i32, ptr %x-2245-18
    %_40 = icmp eq i32 %_38, %_39
    br i1 %_40, label %trueLabel-4, label %toLabel-4
trueLabel-4:
    br label %returnLabel
toLabel-4:
    %_41 = getelementptr %class-Heap_Node, ptr %this, i32 0, i32 0
    %_42 = load ptr, ptr %_41
    %_43 = load i32, ptr %x-2245-18
    %_44 = load i32, ptr %largest-2248-8
    call void @Array_Node.swap(ptr %_42, i32 %_43, i32 %_44)
    %_45 = load i32, ptr %largest-2248-8
    call void @Heap_Node.maxHeapify(ptr %this, i32 %_45)
    br label %returnLabel
returnLabel:
    ret void
}

define void @init() {
entry:
    %i-2277-6 = alloca i32
    %u-2279-8 = alloca i32
    %v-2280-8 = alloca i32
    %w-2281-8 = alloca i32
    %_0 = call i32 @getInt()
    store i32 %_0, ptr @n
    %_1 = call i32 @getInt()
    store i32 %_1, ptr @m
    %_2 = call ptr @.init-class-EdgeList()
    store ptr %_2, ptr @g
    %_3 = load ptr, ptr @g
    %_4 = load i32, ptr @n
    %_5 = load i32, ptr @m
    call void @EdgeList.init(ptr %_3, i32 %_4, i32 %_5)
    store i32 0, ptr %i-2277-6
    store i32 0, ptr %i-2277-6
    br label %loopCondition-2278-2
loopCondition-2278-2:
    %_6 = load i32, ptr %i-2277-6
    %_7 = load i32, ptr @m
    %_8 = icmp slt i32 %_6, %_7
    br i1 %_8, label %loopBody-2278-2, label %loopTo-2278-2
loopBody-2278-2:
    %_9 = call i32 @getInt()
    store i32 %_9, ptr %u-2279-8
    %_10 = call i32 @getInt()
    store i32 %_10, ptr %v-2280-8
    %_11 = call i32 @getInt()
    store i32 %_11, ptr %w-2281-8
    %_12 = load ptr, ptr @g
    %_13 = load i32, ptr %u-2279-8
    %_14 = load i32, ptr %v-2280-8
    %_15 = load i32, ptr %w-2281-8
    call void @EdgeList.addEdge(ptr %_12, i32 %_13, i32 %_14, i32 %_15)
    br label %loopStep-2278-2
loopStep-2278-2:
    %_16 = load i32, ptr %i-2277-6
    %_17 = add i32 %_16, 1
    store i32 %_17, ptr %i-2277-6
    br label %loopCondition-2278-2
loopTo-2278-2:
    br label %returnLabel
returnLabel:
    ret void
}

define ptr @.init-class-Node() {
entry:
    %this = call ptr @.malloc(i32 2)
    %_0 = getelementptr %class-Node, ptr %this, i32 0, i32 0
    store i32 0, ptr %_0
    %_1 = getelementptr %class-Node, ptr %this, i32 0, i32 1
    store i32 0, ptr %_1
    br label %returnLabel
returnLabel:
    ret ptr %this
}

define i32 @Node.key_(ptr %this) {
entry:
    %.returnValue = alloca i32
    store i32 0, ptr %.returnValue
    %_0 = getelementptr %class-Node, ptr %this, i32 0, i32 1
    %_1 = load i32, ptr %_0
    %_2 = sub i32 0, %_1
    store i32 %_2, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_3 = load i32, ptr %.returnValue
    ret i32 %_3
}

define ptr @dijkstra(i32 %_0) {
entry:
    %.returnValue = alloca ptr
    %s-2296-15 = alloca i32
    %visited-2297-8 = alloca ptr
    %d-2298-8 = alloca ptr
    %i-2299-6 = alloca i32
    %q-2306-12 = alloca ptr
    %src-2307-7 = alloca ptr
    %node-2313-9 = alloca ptr
    %u-2314-8 = alloca i32
    %k-2318-8 = alloca i32
    %v-2320-10 = alloca i32
    %w-2321-10 = alloca i32
    %alt-2322-10 = alloca i32
    store ptr null, ptr %.returnValue
    store i32 %_0, ptr %s-2296-15
    %_1 = load i32, ptr @n
    %_2 = call ptr @.newArray(i32 %_1)
    store ptr %_2, ptr %visited-2297-8
    %_3 = load i32, ptr @n
    %_4 = call ptr @.newArray(i32 %_3)
    store ptr %_4, ptr %d-2298-8
    store i32 0, ptr %i-2299-6
    store i32 0, ptr %i-2299-6
    br label %loopCondition-2300-2
loopCondition-2300-2:
    %_5 = load i32, ptr %i-2299-6
    %_6 = load i32, ptr @n
    %_7 = icmp slt i32 %_5, %_6
    br i1 %_7, label %loopBody-2300-2, label %loopTo-2300-2
loopBody-2300-2:
    %_8 = load i32, ptr @INF
    %_9 = load i32, ptr %i-2299-6
    %_10 = load ptr, ptr %d-2298-8
    %_11 = getelementptr i32, ptr %_10, i32 %_9
    store i32 %_8, ptr %_11
    %_12 = load i32, ptr %i-2299-6
    %_13 = load ptr, ptr %visited-2297-8
    %_14 = getelementptr i32, ptr %_13, i32 %_12
    store i32 0, ptr %_14
    br label %loopStep-2300-2
loopStep-2300-2:
    %_15 = load i32, ptr %i-2299-6
    %_16 = add i32 %_15, 1
    store i32 %_16, ptr %i-2299-6
    br label %loopCondition-2300-2
loopTo-2300-2:
    %_17 = load i32, ptr %s-2296-15
    %_18 = load ptr, ptr %d-2298-8
    %_19 = getelementptr i32, ptr %_18, i32 %_17
    store i32 0, ptr %_19
    %_20 = call ptr @.init-class-Heap_Node()
    store ptr %_20, ptr %q-2306-12
    %_21 = call ptr @.init-class-Node()
    store ptr %_21, ptr %src-2307-7
    %_22 = load ptr, ptr %src-2307-7
    %_23 = getelementptr %class-Node, ptr %_22, i32 0, i32 1
    store i32 0, ptr %_23
    %_24 = load i32, ptr %s-2296-15
    %_25 = load ptr, ptr %src-2307-7
    %_26 = getelementptr %class-Node, ptr %_25, i32 0, i32 0
    store i32 %_24, ptr %_26
    %_27 = load ptr, ptr %q-2306-12
    %_28 = load ptr, ptr %src-2307-7
    call void @Heap_Node.push(ptr %_27, ptr %_28)
    br label %loopCondition-2312-2
loopCondition-2312-2:
    %_29 = load ptr, ptr %q-2306-12
    %_30 = call i32 @Heap_Node.size(ptr %_29)
    %_31 = icmp ne i32 %_30, 0
    br i1 %_31, label %loopBody-2312-2, label %loopTo-2312-2
loopBody-2312-2:
    %_32 = load ptr, ptr %q-2306-12
    %_33 = call ptr @Heap_Node.pop(ptr %_32)
    store ptr %_33, ptr %node-2313-9
    %_34 = load ptr, ptr %node-2313-9
    %_35 = getelementptr %class-Node, ptr %_34, i32 0, i32 0
    %_36 = load i32, ptr %_35
    store i32 %_36, ptr %u-2314-8
    %_37 = load i32, ptr %u-2314-8
    %_38 = load ptr, ptr %visited-2297-8
    %_39 = getelementptr i32, ptr %_38, i32 %_37
    %_40 = load i32, ptr %_39
    %_41 = icmp eq i32 %_40, 1
    br i1 %_41, label %trueLabel-0, label %toLabel-0
trueLabel-0:
    br label %loopStep-2312-2
toLabel-0:
    %_42 = load i32, ptr %u-2314-8
    %_43 = load ptr, ptr %visited-2297-8
    %_44 = getelementptr i32, ptr %_43, i32 %_42
    store i32 1, ptr %_44
    store i32 0, ptr %k-2318-8
    %_45 = load i32, ptr %u-2314-8
    %_46 = load ptr, ptr @g
    %_47 = getelementptr %class-EdgeList, ptr %_46, i32 0, i32 2
    %_48 = load ptr, ptr %_47
    %_49 = getelementptr i32, ptr %_48, i32 %_45
    %_50 = load i32, ptr %_49
    store i32 %_50, ptr %k-2318-8
    br label %loopCondition-2319-4
loopCondition-2319-4:
    %_51 = load i32, ptr %k-2318-8
    %_52 = icmp ne i32 %_51, -1
    br i1 %_52, label %loopBody-2319-4, label %loopTo-2319-4
loopBody-2319-4:
    %_53 = load i32, ptr %k-2318-8
    %_54 = load ptr, ptr @g
    %_55 = getelementptr %class-EdgeList, ptr %_54, i32 0, i32 0
    %_56 = load ptr, ptr %_55
    %_57 = getelementptr ptr, ptr %_56, i32 %_53
    %_58 = load ptr, ptr %_57
    %_59 = getelementptr %class-Edge, ptr %_58, i32 0, i32 1
    %_60 = load i32, ptr %_59
    store i32 %_60, ptr %v-2320-10
    %_61 = load i32, ptr %k-2318-8
    %_62 = load ptr, ptr @g
    %_63 = getelementptr %class-EdgeList, ptr %_62, i32 0, i32 0
    %_64 = load ptr, ptr %_63
    %_65 = getelementptr ptr, ptr %_64, i32 %_61
    %_66 = load ptr, ptr %_65
    %_67 = getelementptr %class-Edge, ptr %_66, i32 0, i32 2
    %_68 = load i32, ptr %_67
    store i32 %_68, ptr %w-2321-10
    %_69 = load i32, ptr %u-2314-8
    %_70 = load ptr, ptr %d-2298-8
    %_71 = getelementptr i32, ptr %_70, i32 %_69
    %_72 = load i32, ptr %_71
    %_73 = load i32, ptr %w-2321-10
    %_74 = add i32 %_72, %_73
    store i32 %_74, ptr %alt-2322-10
    %_75 = load i32, ptr %alt-2322-10
    %_76 = load i32, ptr %v-2320-10
    %_77 = load ptr, ptr %d-2298-8
    %_78 = getelementptr i32, ptr %_77, i32 %_76
    %_79 = load i32, ptr %_78
    %_80 = icmp sge i32 %_75, %_79
    br i1 %_80, label %trueLabel-1, label %toLabel-1
trueLabel-1:
    br label %loopStep-2319-4
toLabel-1:
    %_81 = load i32, ptr %alt-2322-10
    %_82 = load i32, ptr %v-2320-10
    %_83 = load ptr, ptr %d-2298-8
    %_84 = getelementptr i32, ptr %_83, i32 %_82
    store i32 %_81, ptr %_84
    %_85 = call ptr @.init-class-Node()
    store ptr %_85, ptr %node-2313-9
    %_86 = load i32, ptr %v-2320-10
    %_87 = load ptr, ptr %node-2313-9
    %_88 = getelementptr %class-Node, ptr %_87, i32 0, i32 0
    store i32 %_86, ptr %_88
    %_89 = load i32, ptr %v-2320-10
    %_90 = load ptr, ptr %d-2298-8
    %_91 = getelementptr i32, ptr %_90, i32 %_89
    %_92 = load i32, ptr %_91
    %_93 = load ptr, ptr %node-2313-9
    %_94 = getelementptr %class-Node, ptr %_93, i32 0, i32 1
    store i32 %_92, ptr %_94
    %_95 = load ptr, ptr %q-2306-12
    %_96 = load ptr, ptr %node-2313-9
    call void @Heap_Node.push(ptr %_95, ptr %_96)
    br label %loopStep-2319-4
loopStep-2319-4:
    %_97 = load i32, ptr %k-2318-8
    %_98 = load ptr, ptr @g
    %_99 = getelementptr %class-EdgeList, ptr %_98, i32 0, i32 1
    %_100 = load ptr, ptr %_99
    %_101 = getelementptr i32, ptr %_100, i32 %_97
    %_102 = load i32, ptr %_101
    store i32 %_102, ptr %k-2318-8
    br label %loopCondition-2319-4
loopTo-2319-4:
    br label %loopStep-2312-2
loopStep-2312-2:
    br label %loopCondition-2312-2
loopTo-2312-2:
    %_103 = load ptr, ptr %d-2298-8
    store ptr %_103, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_104 = load ptr, ptr %.returnValue
    ret ptr %_104
}

define i32 @main() {
entry:
    %.returnValue = alloca i32
    %i-2340-6 = alloca i32
    %j-2341-6 = alloca i32
    %d-2343-10 = alloca ptr
    store i32 0, ptr %.returnValue
    call void @init()
    store i32 0, ptr %i-2340-6
    store i32 0, ptr %j-2341-6
    store i32 0, ptr %i-2340-6
    br label %loopCondition-2342-2
loopCondition-2342-2:
    %_0 = load i32, ptr %i-2340-6
    %_1 = load i32, ptr @n
    %_2 = icmp slt i32 %_0, %_1
    br i1 %_2, label %loopBody-2342-2, label %loopTo-2342-2
loopBody-2342-2:
    %_3 = load i32, ptr %i-2340-6
    %_4 = call ptr @dijkstra(i32 %_3)
    store ptr %_4, ptr %d-2343-10
    store i32 0, ptr %j-2341-6
    br label %loopCondition-2344-4
loopCondition-2344-4:
    %_5 = load i32, ptr %j-2341-6
    %_6 = load i32, ptr @n
    %_7 = icmp slt i32 %_5, %_6
    br i1 %_7, label %loopBody-2344-4, label %loopTo-2344-4
loopBody-2344-4:
    %_8 = load i32, ptr %j-2341-6
    %_9 = load ptr, ptr %d-2343-10
    %_10 = getelementptr i32, ptr %_9, i32 %_8
    %_11 = load i32, ptr %_10
    %_12 = load i32, ptr @INF
    %_13 = icmp eq i32 %_11, %_12
    br i1 %_13, label %trueLabel-0, label %falseLabel-0
trueLabel-0:
    call void @print(ptr @constString-0)
    br label %toLabel-0
falseLabel-0:
    %_14 = load i32, ptr %j-2341-6
    %_15 = load ptr, ptr %d-2343-10
    %_16 = getelementptr i32, ptr %_15, i32 %_14
    %_17 = load i32, ptr %_16
    %_18 = call ptr @toString(i32 %_17)
    call void @print(ptr %_18)
    br label %toLabel-0
toLabel-0:
    call void @print(ptr @constString-1)
    br label %loopStep-2344-4
loopStep-2344-4:
    %_19 = load i32, ptr %j-2341-6
    %_20 = add i32 %_19, 1
    store i32 %_20, ptr %j-2341-6
    br label %loopCondition-2344-4
loopTo-2344-4:
    call void @println(ptr @constString-2)
    br label %loopStep-2342-2
loopStep-2342-2:
    %_21 = load i32, ptr %i-2340-6
    %_22 = add i32 %_21, 1
    store i32 %_22, ptr %i-2340-6
    br label %loopCondition-2342-2
loopTo-2342-2:
    store i32 0, ptr %.returnValue
    br label %returnLabel
returnLabel:
    %_23 = load i32, ptr %.returnValue
    ret i32 %_23
}
