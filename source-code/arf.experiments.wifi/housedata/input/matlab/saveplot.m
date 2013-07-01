
figure('visible','on'); %set to off



[nrExperiments, nrDays] =  size(exp);

colors = getColors(nrExperiments);

lineStylesCell={'-','-.', '--',':'};
markerStylesCell={'+', 'o', '.', 'x', '*', 's', 'd'};
index=1;
legendCell = {};

for expNr=1:nrExperiments
    
    
    values = exp(expNr,:);
    
    if min(sum(isnan(values))) == 0
        
        
        plotje = semilogx(datapoints, values);
        set(plotje,'LineStyle',lineStylesCell{index});
        set(plotje, 'Marker',markerStylesCell{index});
        
        set(plotje, 'MarkerFaceColor',colors(index,:));
        
        set(plotje,'Color',colors(index,:),'LineWidth',2);
        
        hold on;
        
        
        
        legendCell{index} = expLegend{expNr};
        
        
        %update colors, legend, line style
        index = index + 1;
        
        
        
    end
end



% fit plot
%axis tight


% set x and y axes
axis([min(datapoints),max(datapoints)+1,0,1])

% set tickmarks
set(gca,'XTick',datapoints)
set(gca,'YTick',[0:0.1:1])

% set x and y axis labels
x=xlabel('Number of labeled days');
set(x, 'FontSize', 12);
set(x,'FontWeight','bold');

y=ylabel('Avg. Accuracy');
set(y, 'FontSize', 12);
set(y,'FontWeight','bold');

% set legend
l = legend(legendCell,'Location','SouthEast');
set(l, 'FontSize', 10);
set(l,'FontWeight','bold');



% title
t = title(strcat('House ', houseName));
set(t, 'FontSize', 13);
set(t,'FontWeight','bold');


% remove upper and right axes
box off

%remove legend box
legend boxoff

% save plot
fh = gcf; % get figure handle
saveas(gcf, strcat(directory,strcat('plot',houseName,'.pdf')));
clear fh;
hold off;

