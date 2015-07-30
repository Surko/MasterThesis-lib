%colic_y = [85.16, 85.86, 85.51, 84.83, 82.93];
%colic_e = [5.91;5.59;5.95;5.87;5.8];
%credita_y = [85.57, 85.39, 84.99, 85.77, 85.51];
%credita_e = [3.96;3.81;4.24;3.89;3.96];
%creditg_y = [71.25, 71.7, 70.79, 71.14, 70.0];
%creditg_e = [3.17;2.12;3.14;3.11;0.0];
%hepatitis_y = [79.22, 80.18, 78.78, 79.82, 80.76];
%hepatitis_e = [9.57;8.28;9.05;9.74;8.15];
%iris_y = [94.73, 95.8, 95.0, 94.53, 93.73];
%iris_e = [5.3;4.41;5.14;5.47;5.9];
%labor_y = [78.6, 84.13, 84.7, 84.7, 89.43];
%labor_e = [16.58;15.68;15.8;15.12;13.63];
%lymph_y = [75.84, 72.7, 76.61, 77.36, 69.7];
%lymph_e = [11.05;9.9;9.07;10.37;12.59];
%breast_y = [74.28, 75.02, 73.24, 72.96, 71.45];
%breast_e = [6.05;5.22;6.07;5.95;7.0];

colic_y = [8.8, 6.0, 10.0, 15.0, 5.17];
colic_e = [2.69;0;0;0;0.73];
credita_y = [32.82, 6, 10.07, 15.01, 3.98];
credita_e = [9.9;0;0.29;0.1;1.0];
creditg_y = [126.85, 6.0, 10.0, 15.0, 4.0];
creditg_e = [20.66;0;0;0;0.0];
hepatitis_y = [17.66, 6.82, 10.94, 15.0, 4.98];
hepatitis_e = [4.75;0.58;0.34;0;0.2];
iris_y = [8.28, 7, 9.98, 15.0, 5.0];
iris_e = [1.19;0;1;0;0];
labor_y = [6.92, 6.84, 10.02, 15.0, 5.0];
labor_e = [2.53;0.53;0.2;0;0];
lymph_y = [28.0, 6.73, 10, 15, 4.94];
lymph_e = [4.56;0.45;0.0;0.0;0.21];
breast_y = [12.78, 6.0, 10.0, 15.0, 4.0];
breast_e = [9.37;0;0;0;0];

fig_c = figure
errorbar(colic_y,colic_e,'x')
title('Colic')
ylabel('velkost stromu')
set(gca, 'XTick',1:5, 'XTickLabel',{'J48' 'GA6' 'GA10' 'GA15' 'GA4'})
print('Colic.pdf','-dpdf')

fig_a = figure
errorbar(credita_y,credita_e,'x')
title('Credit-a')
ylabel('velkost stromu')
set(gca, 'XTick',1:5, 'XTickLabel',{'J48' 'GA6' 'GA10' 'GA15' 'GA4'})
print('Credit-a.pdf','-dpdf')

fig_g = figure
errorbar(creditg_y,creditg_e,'x')
title('Credit-g')
ylabel('velkost stromu')
set(gca, 'XTick',1:5, 'XTickLabel',{'J48' 'GA6' 'GA10' 'GA15' 'GA4'})
print('Credit-g.pdf','-dpdf')

figure
errorbar(hepatitis_y,hepatitis_e,'x')
title('Hepat.')
ylabel('velkost stromu')
set(gca, 'XTick',1:5, 'XTickLabel',{'J48' 'GA6' 'GA10' 'GA15' 'GA4'})
print('Hepatitis.pdf','-dpdf')

fig_i = figure
errorbar(iris_y,iris_e,'x')
title('Iris')
ylabel('velkost stromu')
set(gca, 'XTick',1:5, 'XTickLabel',{'J48' 'GA6' 'GA10' 'GA15' 'GA4'})
print('Iris.pdf','-dpdf')

fig_la = figure
errorbar(labor_y,labor_e,'x')
title('Labor')
ylabel('velkost stromu')
set(gca, 'XTick',1:5, 'XTickLabel',{'J48' 'GA6' 'GA10' 'GA15' 'GA4'})
print('Labor.pdf','-dpdf')

fig_ly = figure
errorbar(lymph_y,lymph_e,'x')
title('Lymph')
ylabel('velkost stromu')
set(gca, 'XTick',1:5, 'XTickLabel',{'J48' 'GA6' 'GA10' 'GA15' 'GA4'})
print('Lymph.pdf','-dpdf')

figure
errorbar(breast_y,breast_e,'x')
title('Breast')
ylabel('velkost stromu')
set(gca, 'XTick',1:5, 'XTickLabel',{'J48' 'GA6' 'GA10' 'GA15' 'GA4'})
print('Breast.pdf','-dpdf')