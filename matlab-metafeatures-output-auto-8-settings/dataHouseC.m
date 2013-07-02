exp = [];
expLegend={};
datapoints=[];
expLegend{1}='CT\_ABS PR\_BOTH SSE';
exp(1,1)=0.17402597402597403;
datapoints(1,1)=2;

exp(1,2)=0.23990083046205493;
datapoints(2,1)=3;

exp(1,3)=0.27073829427277707;
datapoints(3,1)=6;

exp(1,4)=0.30915881504116804;
datapoints(4,1)=11;



expLegend{2}='CT\_REL PR\_BOTH KL';
exp(2,1)=0.2810924369747899;
datapoints(1,1)=2;

exp(2,2)=0.22530232448710713;
datapoints(2,1)=3;

exp(2,3)=0.3067970556840418;
datapoints(3,1)=6;

exp(2,4)=0.345009266332796;
datapoints(4,1)=11;



expLegend{3}='CT\_ABS PR\_SP SSE';
exp(3,1)=0.22599312452253625;
datapoints(1,1)=2;

exp(3,2)=0.2653441886396431;
datapoints(2,1)=3;

exp(3,3)=0.27074327057026043;
datapoints(3,1)=6;

exp(3,4)=0.3090846403141487;
datapoints(4,1)=11;



expLegend{4}='CT\_ABS PR\_SP KL';
exp(4,1)=0.22892793481028773;
datapoints(1,1)=2;

exp(4,2)=0.23812747536151782;
datapoints(2,1)=3;

exp(4,3)=0.2580886836168526;
datapoints(3,1)=6;

exp(4,4)=0.2951139024909515;
datapoints(4,1)=11;



expLegend{5}='CT\_ABS PR\_BOTH KL';
exp(5,1)=0.22133732280791102;
datapoints(1,1)=2;

exp(5,2)=0.2477100941386655;
datapoints(2,1)=3;

exp(5,3)=0.26937302810184166;
datapoints(3,1)=6;

exp(5,4)=0.2924238887474182;
datapoints(4,1)=11;



expLegend{6}='CT\_REL PR\_BOTH SSE';
exp(6,1)=0.22425515660809778;
datapoints(1,1)=2;

exp(6,2)=0.26052981109799284;
datapoints(2,1)=3;

exp(6,3)=0.2942618605883915;
datapoints(3,1)=6;

exp(6,4)=0.3488587170405354;
datapoints(4,1)=11;



expLegend{7}='CT\_REL PR\_SP KL';
exp(7,1)=0.16262414056531704;
datapoints(1,1)=2;

exp(7,2)=0.23142806805597504;
datapoints(2,1)=3;

exp(7,3)=0.2717528798884732;
datapoints(3,1)=6;

exp(7,4)=0.2841524488583314;
datapoints(4,1)=11;



expLegend{8}='CT\_REL PR\_SP SSE';
exp(8,1)=0.171606824548001;
datapoints(1,1)=2;

exp(8,2)=0.2532034632034632;
datapoints(2,1)=3;

exp(8,3)=0.29885421745489993;
datapoints(3,1)=6;

exp(8,4)=0.3633702655979885;
datapoints(4,1)=11;




directory='/root/source-code/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' C (transfer)';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;