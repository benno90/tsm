set terminal postscript eps
set output "test.eps"
set title "Ant Colony TSP optimization"


set style line 1 lc rgb "red" lt 1 lw 4
set style line 2 lc rgb "blue" lt 1 lw 4

# ------- parameters ------------
#data = "rl5915_ac.csv"
data = "berlin52_ac.csv"
#data = "bier127_ac.csv"
#data = "reseau_suisse_ac.csv"
#data = "pr1002_ac.csv"
#data = "tsp225_ac.csv"
#data = "sw24978_ac.csv"

# ------- plot  --------
#set title "Fig1"
set xlabel "iterations"
set ylabel "tourlength"
plot data using 1:2 w l ls 2 title "best tour length",\
     data using 1:3 w l ls 1 title "worst tour length of iteration"

# ------- replot ----------------
#pause 300
#reread





#set multiplot
#set size 1, 0.5
#
#set origin 0.0,0.5
#plot sin(x), log(x)
#
#set origin 0.0,0.0
#plot sin(x), log(x), cos(x)
#
#unset multiplot
