import os

# test.testcases = [
#     "sorting/bubble_sort.mx",
#     "sorting/merge_sort.mx",
#     "sorting/quick_sort.mx",
#     "sorting/selection_sort.mx",

#     "shortest_path/dijkstra.mx",
#     "shortest_path/floyd.mx",
#     "shortest_path/spfa.mx",

#     "std/queue.mt",
# ]

test.testcases = [
    "binary_tree.mx",
    "dijkstra.mx",
    "humble.mx",
    "kruskal.mx",
    "lca.mx",
    "lunatic.mx",
    "maxflow.mx",
    "pi.mx",
    "segtree.mx",
    "sha_1.mx"
]


for testcase in test.testcases:
    cmd = "python3 ./preprocess.py " + \
        "../optim2/" + testcase + " > " + \
        "../optim/" + testcase
    os.system(cmd)
