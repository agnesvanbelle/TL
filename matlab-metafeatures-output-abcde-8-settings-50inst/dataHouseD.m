exp = [];
expLegend={};
datapoints=[];
expLegend{1}='CT\_ABS PR\_BOTH SSE';
exp(1,1)=0.21575961075961078;
datapoints(1,1)=2;

exp(1,2)=0.22375495439519819;
datapoints(2,1)=3;

exp(1,3)=0.26636486289511185;
datapoints(3,1)=6;

exp(1,4)=0.32129382698731973;
datapoints(4,1)=11;



expLegend{2}='CT\_REL PR\_BOTH KL';
exp(2,1)=0.17561746245956772;
datapoints(1,1)=2;

exp(2,2)=0.2306923687820374;
datapoints(2,1)=3;

exp(2,3)=0.2376532827944233;
datapoints(3,1)=6;

exp(2,4)=0.24741114279952792;
datapoints(4,1)=11;



expLegend{3}='CT\_ABS PR\_SP SSE';
exp(3,1)=0.20936128686128683;
datapoints(1,1)=2;

exp(3,2)=0.22722673347673344;
datapoints(2,1)=3;

exp(3,3)=0.2509877949982116;
datapoints(3,1)=6;

exp(3,4)=0.3058391561357659;
datapoints(4,1)=11;



expLegend{4}='CT\_ABS PR\_SP KL';
exp(4,1)=0.16325193325193327;
datapoints(1,1)=2;

exp(4,2)=0.22238491565414642;
datapoints(2,1)=3;

exp(4,3)=0.25824917240733564;
datapoints(3,1)=6;

exp(4,4)=0.3199562960579905;
datapoints(4,1)=11;



expLegend{5}='CT\_ABS PR\_BOTH KL';
exp(5,1)=0.14240842490842492;
datapoints(1,1)=2;

exp(5,2)=0.2415948769844873;
datapoints(2,1)=3;

exp(5,3)=0.2604004984213316;
datapoints(3,1)=6;

exp(5,4)=0.32135565968607904;
datapoints(4,1)=11;



expLegend{6}='CT\_REL PR\_BOTH SSE';
exp(6,1)=0.19329344729344727;
datapoints(1,1)=2;

exp(6,2)=0.23204340177366484;
datapoints(2,1)=3;

exp(6,3)=0.2386229040841185;
datapoints(3,1)=6;

exp(6,4)=0.24436837606837641;
datapoints(4,1)=11;



expLegend{7}='CT\_REL PR\_SP KL';
exp(7,1)=0.19629629629629627;
datapoints(1,1)=2;

exp(7,2)=0.2338378864694654;
datapoints(2,1)=3;

exp(7,3)=0.24389931559668443;
datapoints(3,1)=6;

exp(7,4)=0.2731693862505267;
datapoints(4,1)=11;



expLegend{8}='CT\_REL PR\_SP SSE';
exp(8,1)=0.2287942077415761;
datapoints(1,1)=2;

exp(8,2)=0.22036400295610817;
datapoints(2,1)=3;

exp(8,3)=0.24003060507769686;
datapoints(3,1)=6;

exp(8,4)=0.24380750472855756;
datapoints(4,1)=11;




directory='/root/source-code/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' D (transfer)';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;