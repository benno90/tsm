
set terminal postscript eps
set output "test.eps"
set multiplot layout 2, 1 title "Simulated Annealing"

# ------- parameters ------------
N = 5*1000*1000
#data = "rl5915.csv"
data = "berlin52.csv"
#data = "reseau_suisse.csv"
#data = "pr1002.csv"
#data = "tsp225.csv"
#data = "sw24978.csv"

# ------- plot temperature --------
#set title "Fig1"
set xlabel "iterations"
set ylabel "temperature"
set xrange[0:N]
unset key
plot data using 1:2 ls 1 with lines

# ------- plot length -------------
#set title "Fig2"
set ylabel "tour length"
unset key
plot data using 1:3 ls 1 with lines
unset multiplot

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
