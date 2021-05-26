import matplotlib.pyplot as plt
from matplotlib import animation
import numpy as np
from mpl_toolkits.mplot3d import axes3d, art3d
from matplotlib.animation import PillowWriter
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import PySimpleGUI as sg
import matplotlib
matplotlib.use('TkAgg')

path = '/Users/Zecheng Yan/Desktop/Healthcare Coop/Code/KIMORE.Dataset/ID_7/'
muscle = np.genfromtxt(path+"JointPosition_75.csv", delimiter = ',', skip_header = 1)
print(muscle)

xMax, xMin = 0, 0
yMax, yMin = 0, 0
zMax, zMin = 0, 0

for k in range(4, 99, 4):
    muscle[:, k] = muscle[:, k] - np.median(muscle[:,0])
    muscle[:, k+1] = muscle[:, k+1] - np.median(muscle[:,1])
    muscle[:, k+2] = muscle[:, k+2] - np.median(muscle[:,2])

    # Getting Max and Min for x y z coordinates
    xMax = max(xMax, max(muscle[:, k]))
    xMin = min(xMin, min(muscle[:, k]))
    yMax = max(yMax, max(muscle[:, k+2]))
    yMin = min(yMin, min(muscle[:, k+2]))
    zMax = max(zMax, max(muscle[:, k+1]))
    zMin = min(zMin, min(muscle[:, k+1]))

# Swapping y and Z axis
for k in range(0, 99, 4):
    muscle[:, [k+1, k+2]] = muscle[:, [k+2, k+1]]

muscle[:, 0] = muscle[:, 0] - np.median(muscle[:, 0])
muscle[:, 1] = muscle[:, 1] - np.median(muscle[:, 1])
muscle[:, 2] = muscle[:, 2] - np.median(muscle[:, 2])


# index_Spine_Base=1 # index_Spine_Mid=5 # index_Neck=9 # index_Head=13
# index_Shoulder_Left=17 # index_Elbow_Left=21 # index_Wrist_Left=25 # index_Hand_Left=29
# index_Shoulder_Right=33 # index_Elbow_Right=37 # index_Wrist_Right=41 # index_Hand_Right=45
# index_Hip_Left=49
# index_Knee_Left=53 # index_Ankle_Left=57 # index_Foot_Left=61 # index_Hip_Right=65
# index_Knee_Right=69 # index_Ankle_Right=73 # index_Foot_Right=77 # index_Spine_Shoulder=81
# index_Tip_Left=85 # index_Thumb_Left=89 # index_Tip_Right=93 # index_Thumb_Right=97

Joint_name = ['Spine_Base','Spine_Mid','Neck','Head','Shoulder_Left','Elbow_Left',
              'Wrist_Left','Hand_Left','Shoulder_Right','Elbow_Right','Wrist_Right',
              'Hand_Right','Hip_Left', 'Knee_Left','Ankle_Left','Foot_Left','Hip_Right',
              'Knee_Right','Ankle_Right','Foot_Right','Spine_Shoulder','Tip_Left',
              'Thumb_Left','Tip_Right','Thumb_Right']
marker = ['go','go','go','yo','bo','bo','bo','bo','ro','ro','ro','ro','bo','bo','bo','bo',
          'ro','ro','ro','ro','go','bo','bo','ro','ro']
msize = 4*np.ones([25,1])
msize[3] = 12

Joint_Index = np.array([1,5,9,13,17,21,25,29,33,37,41,45,49,53,57,61,65,69,73,77,81,85,89,93,97])
Joint_Link = [0]*len(Joint_Index)
Joint_Link[0] = np.array([np.where(np.char.find(Joint_name, 'Wrist_Left')>=0),
                 np.where(np.char.find(Joint_name, 'Elbow_Left')>=0)])
Joint_Link[1] = np.array([np.where(np.char.find(Joint_name, 'Elbow_Left')>=0),
                 np.where(np.char.find(Joint_name, 'Shoulder_Left')>=0)])
Joint_Link[2] = np.array([np.where(np.char.find(Joint_name, 'Wrist_Right')>=0),
                 np.where(np.char.find(Joint_name, 'Elbow_Right')>=0)])
Joint_Link[3] = np.array([np.where(np.char.find(Joint_name, 'Elbow_Right')>=0),
                 np.where(np.char.find(Joint_name, 'Shoulder_Right')>=0)])
Joint_Link[4] = np.array([np.where(np.char.find(Joint_name, 'Shoulder_Left')>=0),
                 np.where(np.char.find(Joint_name, 'Spine_Shoulder')>=0)])
Joint_Link[5] = np.array([np.where(np.char.find(Joint_name, 'Spine_Shoulder')>=0),
                 np.where(np.char.find(Joint_name, 'Shoulder_Right')>=0)])
Joint_Link[6] = np.array([np.where(np.char.find(Joint_name, 'Neck')>=0),
                 np.where(np.char.find(Joint_name, 'Spine_Shoulder')>=0)])
Joint_Link[7] = np.array([np.where(np.char.find(Joint_name, 'Spine_Shoulder')>=0),
                 np.where(np.char.find(Joint_name, 'Spine_Mid')>=0)])
Joint_Link[8] = np.array([np.where(np.char.find(Joint_name, 'Spine_Mid')>=0),
                 np.where(np.char.find(Joint_name, 'Spine_Base')>=0)])
Joint_Link[9] = np.array([np.where(np.char.find(Joint_name, 'Spine_Base')>=0),
                 np.where(np.char.find(Joint_name, 'Hip_Left')>=0)])
Joint_Link[10] = np.array([np.where(np.char.find(Joint_name, 'Spine_Base')>=0),
                 np.where(np.char.find(Joint_name, 'Hip_Right')>=0)])
Joint_Link[11] = np.array([np.where(np.char.find(Joint_name, 'Hip_Left')>=0),
                 np.where(np.char.find(Joint_name, 'Knee_Left')>=0)])
Joint_Link[12] = np.array([np.where(np.char.find(Joint_name, 'Hip_Right')>=0),
                 np.where(np.char.find(Joint_name, 'Knee_Right')>=0)])
Joint_Link[13] = np.array([np.where(np.char.find(Joint_name, 'Ankle_Left')>=0),
                 np.where(np.char.find(Joint_name, 'Knee_Left')>=0)])
Joint_Link[14] = np.array([np.where(np.char.find(Joint_name, 'Ankle_Right')>=0),
                 np.where(np.char.find(Joint_name, 'Knee_Right')>=0)])
Joint_Link[15] = np.array([np.where(np.char.find(Joint_name, 'Ankle_Left')>=0),
                 np.where(np.char.find(Joint_name, 'Foot_Left')>=0)])
Joint_Link[16] = np.array([np.where(np.char.find(Joint_name, 'Ankle_Right')>=0),
                 np.where(np.char.find(Joint_name, 'Foot_Right')>=0)])
Joint_Link[17] = np.array([np.where(np.char.find(Joint_name, 'Neck')>=0),
                 np.where(np.char.find(Joint_name, 'Head')>=0)])
Joint_Link[18] = np.array([np.where(np.char.find(Joint_name, 'Wrist_Left')>=0),
                 np.where(np.char.find(Joint_name, 'Hand_Left')>=0)])
Joint_Link[19] = np.array([np.where(np.char.find(Joint_name, 'Wrist_Right')>=0),
                 np.where(np.char.find(Joint_name, 'Hand_Right')>=0)])
Joint_Link[20] = np.array([np.where(np.char.find(Joint_name, 'Hand_Left')>=0),
                 np.where(np.char.find(Joint_name, 'Thumb_Left')>=0)])
Joint_Link[21] = np.array([np.where(np.char.find(Joint_name, 'Hand_Right')>=0),
                 np.where(np.char.find(Joint_name, 'Thumb_Right')>=0)])
Joint_Link[22] = np.array([np.where(np.char.find(Joint_name, 'Hand_Left')>=0),
                 np.where(np.char.find(Joint_name, 'Tip_Left')>=0)])
Joint_Link[23] = np.array([np.where(np.char.find(Joint_name, 'Hand_Right')>=0),
                 np.where(np.char.find(Joint_name, 'Tip_Right')>=0)])

data = [0]*len(Joint_Index)

dataPoint = [0]*len(Joint_Index)
fig = plt.figure()
ax = axes3d.Axes3D(fig)

for j in range(len(Joint_Index)):
    data[j] = np.vstack((np.array(np.transpose(muscle[:, Joint_Index[j]-1])),
                     np.array(np.transpose(muscle[:, Joint_Index[j]])),
                     np.array(np.transpose(muscle[:, Joint_Index[j]+1]))))
    dataPoint[j], = ax.plot(data[j][0, 0:1], data[j][1,0:1], data[j][2, 0:1], marker[j],
                            markersize = msize[j])

Data_Link = [0]*24
for j in range(24):
    bs = int(Joint_Link[j][0])
    bn = int(Joint_Link[j][1])
    Data_Link[j] = art3d.Line3D([data[bs][0,0], data[bn][0,0]], [data[bs][1,0],
                                data[bn][1,0]], [data[bs][2,0], data[bn][2,0]])
    ax.add_line(Data_Link[j])



animationFrameCount = int(data[3][0].shape[0])
animationFrameCount -=1


def animationFrame(i, data,dataPoint,Joint_Link,Data_Link):
    for j in range(25):
        dataPoint[j].set_data(data[j][:2, i])
        dataPoint[j].set_3d_properties(data[j][2, i])
    for j in range(24):
        bs = int(Joint_Link[j][0])
        bn = int(Joint_Link[j][1])
        Data_Link[j].set_data_3d([data[bs][0,i], data[bn][0,i]], [data[bs][1,i],
                                data[bn][1,i]], [data[bs][2,i], data[bn][2,i]])

animation = animation.FuncAnimation(fig, animationFrame, frames = animationFrameCount,
                                    fargs=(data, dataPoint,Joint_Link,Data_Link),interval=1 )
ax.set_xlabel('X')
ax.set_ylabel('Y')
ax.set_zlabel('Z')
# Setting limits for x y z
# Subtracting and adding a small number to give it more space
xMin -= 0.1
yMin -= 0.3
# zMin -= 0.1
xMax += 0.1
yMax += 0.2
zMax += 0.1
ax.set_xlim3d([xMin, xMax])
ax.set_ylim3d([yMin, yMax])
ax.set_zlim3d([zMin, zMax])

# ------------------------------- Beginning of Matplotlib helper code -----------------------

def draw_figure(canvas, figure):
    figure_canvas_agg = FigureCanvasTkAgg(figure, canvas)
    figure_canvas_agg.draw()
    figure_canvas_agg.get_tk_widget().pack(side='top', fill='both', expand=1)
    return figure_canvas_agg

# ------------------------------- Beginning of GUI CODE -------------------------------

# define the window layout
layout = [[sg.Text('Plot test')],
          [sg.Canvas(key='-CANVAS-')],
          [sg.Button('Ok')]]

# create the form and show it without the plot
window = sg.Window('Demo Application - Embedding Matplotlib In PySimpleGUI', layout, finalize=True, element_justification='center', font='Helvetica 18')

# add the plot to the window
fig_canvas_agg = draw_figure(window['-CANVAS-'].TKCanvas, fig)

event, values = window.read()

window.close()

#will need this helper function later when integrating with jolle's template
def getFigure():
    return fig

#will need this when imported as a module, or else the matplot will automatically run
if __name__ == '__main__':
  plt.show()

# Save animation as gif
# animation.save('patient1ex1.gif', writer = 'Pillow', fps = 60)
