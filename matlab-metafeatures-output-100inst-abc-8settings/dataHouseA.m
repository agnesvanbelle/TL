exp = [];
expLegend={};
datapoints=[];
expLegend{1}='CT\_ABS PR\_BOTH SSE';
exp(1,1)=0.41985157699443415;
datapoints(1,1)=2;

exp(1,2)=0.4306544105245402;
datapoints(2,1)=3;

exp(1,3)=0.5421209102982;
datapoints(3,1)=6;

exp(1,4)=0.6118195649322127;
datapoints(4,1)=11;

exp(1,5)=0.6888156230939736;
datapoints(5,1)=21;


expLegend{2}='CT\_REL PR\_BOTH KL';
exp(2,1)=0.4565484779770494;
datapoints(1,1)=2;

exp(2,2)=0.44274583817266716;
datapoints(2,1)=3;

exp(2,3)=0.5104575841102783;
datapoints(3,1)=6;

exp(2,4)=0.6072376788779943;
datapoints(4,1)=11;

exp(2,5)=0.642190613619185;
datapoints(5,1)=21;


expLegend{3}='CT\_ABS PR\_SP SSE';
exp(3,1)=0.39511097368240217;
datapoints(1,1)=2;

exp(3,2)=0.3810833199722087;
datapoints(2,1)=3;

exp(3,3)=0.44453748767376056;
datapoints(3,1)=6;

exp(3,4)=0.4890731852688363;
datapoints(4,1)=11;

exp(3,5)=0.5409772326438997;
datapoints(5,1)=21;


expLegend{4}='CT\_ABS PR\_SP KL';
exp(4,1)=0.4099292242149386;
datapoints(1,1)=2;

exp(4,2)=0.44265655696378575;
datapoints(2,1)=3;

exp(4,3)=0.5504786879786872;
datapoints(3,1)=6;

exp(4,4)=0.6506403675376004;
datapoints(4,1)=11;

exp(4,5)=0.7452801827801828;
datapoints(5,1)=21;


expLegend{5}='CT\_ABS PR\_BOTH KL';
exp(5,1)=0.4040610183467327;
datapoints(1,1)=2;

exp(5,2)=0.4178952345101411;
datapoints(2,1)=3;

exp(5,3)=0.5540544413024244;
datapoints(3,1)=6;

exp(5,4)=0.6247494035117789;
datapoints(4,1)=11;

exp(5,5)=0.6816892527762094;
datapoints(5,1)=21;


expLegend{6}='CT\_REL PR\_BOTH SSE';
exp(6,1)=0.4231258159829588;
datapoints(1,1)=2;

exp(6,2)=0.46067868224730946;
datapoints(2,1)=3;

exp(6,3)=0.5199307388052405;
datapoints(3,1)=6;

exp(6,4)=0.602961931568651;
datapoints(4,1)=11;

exp(6,5)=0.6535249535249537;
datapoints(5,1)=21;


expLegend{7}='CT\_REL PR\_SP KL';
exp(7,1)=0.3822751322751323;
datapoints(1,1)=2;

exp(7,2)=0.4367445267752014;
datapoints(2,1)=3;

exp(7,3)=0.5814380175825953;
datapoints(3,1)=6;

exp(7,4)=0.6391147499843146;
datapoints(4,1)=11;

exp(7,5)=0.6722207906731718;
datapoints(5,1)=21;


expLegend{8}='CT\_REL PR\_SP SSE';
exp(8,1)=0.40669277812134946;
datapoints(1,1)=2;

exp(8,2)=0.45656720580033466;
datapoints(2,1)=3;

exp(8,3)=0.5491594956533978;
datapoints(3,1)=6;

exp(8,4)=0.6187040506704536;
datapoints(4,1)=11;

exp(8,5)=0.6204908979908978;
datapoints(5,1)=21;



directory='/root/source-code1c/experiments/../arf.experiments.wifi/housedata/output/matlab/';
houseName=' A (transfer)';
addpath ../../input/matlab/
run ../../input/matlab/saveplot;