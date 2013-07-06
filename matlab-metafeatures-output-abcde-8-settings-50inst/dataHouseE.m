exp = [];
expLegend={};
datapoints=[];
expLegend{1}='CT\_ABS PR\_BOTH SSE';
exp(1,1)=0.25776311379252553;
datapoints(1,1)=2;

exp(1,2)=0.2552798059783354;
datapoints(2,1)=3;

exp(1,3)=0.28281509728445187;
datapoints(3,1)=6;

exp(1,4)=0.30920253369248824;
datapoints(4,1)=11;



expLegend{2}='CT\_REL PR\_BOTH KL';
exp(2,1)=0.25741462241462243;
datapoints(1,1)=2;

exp(2,2)=0.2731024180370584;
datapoints(2,1)=3;

exp(2,3)=0.28277670872872124;
datapoints(3,1)=6;

exp(2,4)=0.2905423497520941;
datapoints(4,1)=11;



expLegend{3}='CT\_ABS PR\_SP SSE';
exp(3,1)=0.25840737693678867;
datapoints(1,1)=2;

exp(3,2)=0.27595493510718416;
datapoints(2,1)=3;

exp(3,3)=0.2876749285766453;
datapoints(3,1)=6;

exp(3,4)=0.2980839807113628;
datapoints(4,1)=11;



expLegend{4}='CT\_ABS PR\_SP KL';
exp(4,1)=0.27367913132619015;
datapoints(1,1)=2;

exp(4,2)=0.2637880300265133;
datapoints(2,1)=3;

exp(4,3)=0.2853430697610926;
datapoints(3,1)=6;

exp(4,4)=0.30355693881392054;
datapoints(4,1)=11;



expLegend{5}='CT\_ABS PR\_BOTH KL';
exp(5,1)=0.2576567876567876;
datapoints(1,1)=2;

exp(5,2)=0.2750366109597151;
datapoints(2,1)=3;

exp(5,3)=0.285026569759334;
datapoints(3,1)=6;

exp(5,4)=0.30706335574277743;
datapoints(4,1)=11;



expLegend{6}='CT\_REL PR\_BOTH SSE';
exp(6,1)=0.21598235098235097;
datapoints(1,1)=2;

exp(6,2)=0.26693544073062137;
datapoints(2,1)=3;

exp(6,3)=0.2763078737170904;
datapoints(3,1)=6;

exp(6,4)=0.2898988503045298;
datapoints(4,1)=11;



expLegend{7}='CT\_REL PR\_SP KL';
exp(7,1)=0.18573390894819464;
datapoints(1,1)=2;

exp(7,2)=0.27236807514381034;
datapoints(2,1)=3;

exp(7,3)=0.2617422041305011;
datapoints(3,1)=6;

exp(7,4)=0.3011010680769858;
datapoints(4,1)=11;



expLegend{8}='CT\_REL PR\_SP SSE';
exp(8,1)=0.2589488942430119;
datapoints(1,1)=2;

exp(8,2)=0.26391094812050686;
datapoints(2,1)=3;

exp(8,3)=0.27772841241789814;
datapoints(3,1)=6;

exp(8,4)=0.29871751029839266;
datapoints(4,1)=11;




directory='/root/source-code/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' E (transfer)';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;