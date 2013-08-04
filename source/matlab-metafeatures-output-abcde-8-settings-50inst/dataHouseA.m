exp = [];
expLegend={};
datapoints=[];
expLegend{1}='CT\_ABS PR\_BOTH SSE';
exp(1,1)=0.3395485466914039;
datapoints(1,1)=2;

exp(1,2)=0.3935772288713462;
datapoints(2,1)=3;

exp(1,3)=0.4144864797038715;
datapoints(3,1)=6;

exp(1,4)=0.44729284964674626;
datapoints(4,1)=11;

exp(1,5)=0.5874729437229437;
datapoints(5,1)=21;


expLegend{2}='CT\_REL PR\_BOTH KL';
exp(2,1)=0.37705284133855566;
datapoints(1,1)=2;

exp(2,2)=0.42141972950796436;
datapoints(2,1)=3;

exp(2,3)=0.4441477604521083;
datapoints(3,1)=6;

exp(2,4)=0.5205264612732144;
datapoints(4,1)=11;

exp(2,5)=0.6433497925685425;
datapoints(5,1)=21;


expLegend{3}='CT\_ABS PR\_SP SSE';
exp(3,1)=0.41429112554112557;
datapoints(1,1)=2;

exp(3,2)=0.3954473304473302;
datapoints(2,1)=3;

exp(3,3)=0.4053299049943349;
datapoints(3,1)=6;

exp(3,4)=0.4594425235659001;
datapoints(4,1)=11;

exp(3,5)=0.5943810283283967;
datapoints(5,1)=21;


expLegend{4}='CT\_ABS PR\_SP KL';
exp(4,1)=0.441002886002886;
datapoints(1,1)=2;

exp(4,2)=0.39535272619384754;
datapoints(2,1)=3;

exp(4,3)=0.4160642135642138;
datapoints(3,1)=6;

exp(4,4)=0.4476383735312313;
datapoints(4,1)=11;

exp(4,5)=0.5899104136604136;
datapoints(5,1)=21;


expLegend{5}='CT\_ABS PR\_BOTH KL';
exp(5,1)=0.37245928674500106;
datapoints(1,1)=2;

exp(5,2)=0.40429614390010393;
datapoints(2,1)=3;

exp(5,3)=0.4138508769381254;
datapoints(3,1)=6;

exp(5,4)=0.46337177432956705;
datapoints(4,1)=11;

exp(5,5)=0.5883664858348403;
datapoints(5,1)=21;


expLegend{6}='CT\_REL PR\_BOTH SSE';
exp(6,1)=0.3985432556861128;
datapoints(1,1)=2;

exp(6,2)=0.4048549403812558;
datapoints(2,1)=3;

exp(6,3)=0.4303984233514436;
datapoints(3,1)=6;

exp(6,4)=0.5220954910889972;
datapoints(4,1)=11;

exp(6,5)=0.634125085440875;
datapoints(5,1)=21;


expLegend{7}='CT\_REL PR\_SP KL';
exp(7,1)=0.4306878306878307;
datapoints(1,1)=2;

exp(7,2)=0.415256705910911;
datapoints(2,1)=3;

exp(7,3)=0.41579457082812815;
datapoints(3,1)=6;

exp(7,4)=0.5310784093251623;
datapoints(4,1)=11;

exp(7,5)=0.576938890034128;
datapoints(5,1)=21;


expLegend{8}='CT\_REL PR\_SP SSE';
exp(8,1)=0.3756751185322613;
datapoints(1,1)=2;

exp(8,2)=0.3981474800919243;
datapoints(2,1)=3;

exp(8,3)=0.4289921022303571;
datapoints(3,1)=6;

exp(8,4)=0.5291208701111295;
datapoints(4,1)=11;

exp(8,5)=0.6265956265956266;
datapoints(5,1)=21;



directory='/root/source-code/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' A (transfer)';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;