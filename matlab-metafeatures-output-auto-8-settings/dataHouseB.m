exp = [];
expLegend={};
datapoints=[];
expLegend{1}='CT\_ABS PR\_BOTH SSE';
exp(1,1)=0.18686507936507935;
datapoints(1,1)=2;

exp(1,2)=0.17730536659108087;
datapoints(2,1)=3;

exp(1,3)=0.16750364896916606;
datapoints(3,1)=6;

exp(1,4)=0.15829365079365082;
datapoints(4,1)=11;



expLegend{2}='CT\_REL PR\_BOTH KL';
exp(2,1)=0.19940476190476192;
datapoints(1,1)=2;

exp(2,2)=0.20276854928017718;
datapoints(2,1)=3;

exp(2,3)=0.17917122787812426;
datapoints(3,1)=6;

exp(2,4)=0.19670008354218882;
datapoints(4,1)=11;



expLegend{3}='CT\_ABS PR\_SP SSE';
exp(3,1)=0.15833333333333335;
datapoints(1,1)=2;

exp(3,2)=0.15959435626102292;
datapoints(2,1)=3;

exp(3,3)=0.1779131652661063;
datapoints(3,1)=6;

exp(3,4)=0.15829365079365082;
datapoints(4,1)=11;



expLegend{4}='CT\_ABS PR\_SP KL';
exp(4,1)=0.12793650793650793;
datapoints(1,1)=2;

exp(4,2)=0.18105158730158732;
datapoints(2,1)=3;

exp(4,3)=0.161392290249433;
datapoints(3,1)=6;

exp(4,4)=0.12487468671679199;
datapoints(4,1)=11;



expLegend{5}='CT\_ABS PR\_BOTH KL';
exp(5,1)=0.14301587301587299;
datapoints(1,1)=2;

exp(5,2)=0.13785173160173161;
datapoints(2,1)=3;

exp(5,3)=0.18635741652983012;
datapoints(3,1)=6;

exp(5,4)=0.15829365079365082;
datapoints(4,1)=11;



expLegend{6}='CT\_REL PR\_BOTH SSE';
exp(6,1)=0.10472222222222223;
datapoints(1,1)=2;

exp(6,2)=0.16843434343434346;
datapoints(2,1)=3;

exp(6,3)=0.18858896271686953;
datapoints(3,1)=6;

exp(6,4)=0.20115079365079364;
datapoints(4,1)=11;



expLegend{7}='CT\_REL PR\_SP KL';
exp(7,1)=0.22999999999999998;
datapoints(1,1)=2;

exp(7,2)=0.1835825027685493;
datapoints(2,1)=3;

exp(7,3)=0.1547313797313796;
datapoints(3,1)=6;

exp(7,4)=0.17257936507936508;
datapoints(4,1)=11;



expLegend{8}='CT\_REL PR\_SP SSE';
exp(8,1)=0.13865079365079364;
datapoints(1,1)=2;

exp(8,2)=0.13842746400885936;
datapoints(2,1)=3;

exp(8,3)=0.16056953179594674;
datapoints(3,1)=6;

exp(8,4)=0.20115079365079364;
datapoints(4,1)=11;




directory='/root/source-code/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' B (transfer)';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;