# Union-Find (UF) Visualization

School project, 2018-17

## What does the application do

The application presents the Union-Find (UF) Visualization.

It lets the user initialize the number (N) of the nodes to be painted on the pane.
Each of them has unique color, unique id (from 0 to N) and belongs to its own set with no more nodes.
Further the user may create the edge between two of the nodes, 
so the sets, to which the chosen nodes belong, would be merged to one according to the chosen UF implementation.
The nodes of the resulting set get the same color and the new edges between the nodes are drawn.

## How is the application used

Firstly the user should choose the number of the nodes via the input field under the "Number of nodes" label.
He can create new nodes in their initial state any time. The optimal maximal number of the nodes is 45-50.
After this action he may add the edges between the nodes via the input fields under the "Two nodes union" label.
The default configuration of the implementation if Union by Rank and Find by Path Compression.
The user may change the implementation any time by clicking the corresponding button in the menu.
The messages for the user are shown on the bottom panel.
The user may leave the application by clicking the "Exit" button on the right top of the window.

## Authors

* **Tamara Savkova** - *Initial work* - [s-thora](https://github.com/s-thora)
