exp = [];
expLegend={};
datapoints=[];
expLegend{1}='CT\_ABS PR\_BOTH SSE';
exp(1,1)=0.22214285714285714;
datapoints(1,1)=2;

exp(1,2)=0.15886243386243387;
datapoints(2,1)=3;

exp(1,3)=0.1789754363283774;
datapoints(3,1)=6;

exp(1,4)=0.18686507936507937;
datapoints(4,1)=11;



expLegend{2}='CT\_REL PR\_BOTH KL';
exp(2,1)=0.15714285714285714;
datapoints(1,1)=2;

exp(2,2)=0.1686056998556999;
datapoints(2,1)=3;

exp(2,3)=0.1772588522588523;
datapoints(3,1)=6;

exp(2,4)=0.18686507936507937;
datapoints(4,1)=11;



expLegend{3}='CT\_ABS PR\_SP SSE';
exp(3,1)=0.13607142857142857;
datapoints(1,1)=2;

exp(3,2)=0.15369488536155207;
datapoints(2,1)=3;

exp(3,3)=0.17806686777275008;
datapoints(3,1)=6;

exp(3,4)=0.21619047619047613;
datapoints(4,1)=11;



expLegend{4}='CT\_ABS PR\_SP KL';
exp(4,1)=0.1682142857142857;
datapoints(1,1)=2;

exp(4,2)=0.19790123456790124;
datapoints(2,1)=3;

exp(4,3)=0.17922146636432346;
datapoints(3,1)=6;

exp(4,4)=0.20115079365079364;
datapoints(4,1)=11;



expLegend{5}='CT\_ABS PR\_BOTH KL';
exp(5,1)=0.1832936507936508;
datapoints(1,1)=2;

exp(5,2)=0.1385273368606702;
datapoints(2,1)=3;

exp(5,3)=0.18538269188954118;
datapoints(3,1)=6;

exp(5,4)=0.18686507936507937;
datapoints(4,1)=11;



expLegend{6}='CT\_REL PR\_BOTH SSE';
exp(6,1)=0.16936507936507933;
datapoints(1,1)=2;

exp(6,2)=0.17572310405643737;
datapoints(2,1)=3;

exp(6,3)=0.20775596178439776;
datapoints(3,1)=6;

exp(6,4)=0.18686507936507937;
datapoints(4,1)=11;



expLegend{7}='CT\_REL PR\_SP KL';
exp(7,1)=0.1632936507936508;
datapoints(1,1)=2;

exp(7,2)=0.15563492063492065;
datapoints(2,1)=3;

exp(7,3)=0.21098382749326158;
datapoints(3,1)=6;

exp(7,4)=0.23865079365079364;
datapoints(4,1)=11;



expLegend{8}='CT\_REL PR\_SP SSE';
exp(8,1)=0.15;
datapoints(1,1)=2;

exp(8,2)=0.15438271604938275;
datapoints(2,1)=3;

exp(8,3)=0.1888778146917681;
datapoints(3,1)=6;

exp(8,4)=0.30904761904761896;
datapoints(4,1)=11;




directory='/root/source-code1c/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' B (transfer)';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;