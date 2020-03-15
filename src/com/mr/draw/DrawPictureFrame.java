package com.mr.draw;
import  javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import com.mr.util.FrameGetShape;
import com.mr.util.ShapeWindow;
import com.mr.util.Shapes;
import com.mr.util.DrawImageUtil;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.AlphaComposite;
import java.awt.Font;
import javax.swing.JOptionPane;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Cursor;

/*
* 画图主窗体
* */
public class DrawPictureFrame extends JFrame implements FrameGetShape{                                //继承窗体类
    //创建一个8位BGR颜色分量的图像
    BufferedImage image = new BufferedImage(570, 390, BufferedImage.TYPE_INT_BGR);
    Graphics gs = image.getGraphics();                                       //获得图像的绘图对象
    Graphics2D g = (Graphics2D) gs;                                          //将绘图对象转换为Graphics2D类型
    DrawPictureCanvas canvas = new DrawPictureCanvas();                      //创建画布对象
    Color foreColor = Color.BLACK;                                           //定义前景色
    Color backgroundColor = Color.WHITE;                                     //定义背景色
    int x = -1;                                                              //上一次鼠标绘制点的横坐标
    int y = -1;                                                              //上一次鼠标绘制点的纵坐标
    boolean rubber = false;                                                   //橡皮标识变量
    private JToolBar toolBar;                                                //工具栏
    private JButton eraserButton;                                            //橡皮按钮
    private JToggleButton strokeButton1;                                     //细线按钮
    private JToggleButton strokeButton2;                                     //粗线按钮
    private JToggleButton strokeButton3;                                     //较粗按钮
    private JButton backgroundButton;                                        //背景色按钮
    private JButton foregroundButton;                                        //前景色按钮
    private JButton clearButton;                                             //清楚按钮
    private JButton saveButton;                                              //保存按钮
    private JButton shapeButton;                                             //图形按钮
    boolean drawShape = false;                                               //画图形标识变量
    Shapes shape;                                                            //绘画的图形

    private JMenuItem strokeMenuItem1;                                       //细线菜单
    private JMenuItem strokeMenuItem2;                                       //粗线菜单
    private JMenuItem strokeMenuItem3;                                       //较粗菜单
    private JMenuItem clearMenuItem;                                         //清除菜单
    private JMenuItem foregroundMenuItem;                                    //前景色菜单
    private JMenuItem backgroundMenuItem;                                    //背景色菜单
    private JMenuItem eraserMenuItem;                                        //橡皮菜单
    private JMenuItem exitMenuItem;                                          //退出菜单
    private JMenuItem saveMenuItem;                                          //保存菜单

    private JMenuItem shuiyinMenuItem;                                       //水印菜单
    private String shuiyin = "";                                             //水印字符内容

    private PictureWindow pictureWindow;                                     //简笔画展示窗体
    private JButton showPicButton;                                           //展开简笔画按钮

    /*
    * 构造方法，添加组件监听方法
    * */
    public  DrawPictureFrame(){
        setResizable(false);                                                 //窗体不能改变大小
        setTitle("画图程序(水印内容:[" + shuiyin +"])");                      //设置标题，添加水印内容提示
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                      //窗体关闭则停止程序
        setBounds(500, 100, 574, 460);                  //设置窗口位置和宽高

        init();                                                              //组件初始化
        addListener();
    }

    /*
    组件初始化，给窗体添加工具栏及工具栏上的按钮
    * */
    public void init(){
        g.setColor(backgroundColor);                                         //用背景色设置绘图对象的颜色
        g.fillRect(0,0,570, 390);                       //用背景色填充整个画布
        g.setColor(foreColor);                                               //用前景色设置绘图对象的颜色
        canvas.setImage(image);                                              //设置画布图像
        getContentPane().add(canvas);                                        //将画布添加到窗体容器默认布局的中部位置

        toolBar = new JToolBar();                                            //初始化工具栏
        getContentPane().add(toolBar, BorderLayout.NORTH);                   //初始化按钮对象，并添加文本内容
        showPicButton = new JButton("展开简笔画");
        toolBar.add(showPicButton);
        saveButton = new JButton("保存");
        toolBar.add(saveButton);                                             //工具栏添加按钮
        toolBar.addSeparator();                                              //添加分割线条
        //初始化按钮对象，并添加文本内容
        strokeButton1 = new JToggleButton("细线");
        strokeButton1.setSelected(true);                                     //细线按钮处于被选中状态
        toolBar.add(strokeButton1);                                          //工具栏添加按钮
        //初始化按钮对象，并添加文本内容
        strokeButton2 = new JToggleButton("粗线");
        toolBar.add(strokeButton2);                                         //工具栏添加按钮
        //初始化有选中状态的按钮对象，并添加文本内容
        strokeButton3 = new JToggleButton("较粗");
        //画笔粗细按钮组，保证同时只有一个按钮被选中
        ButtonGroup strokeGroup = new ButtonGroup();
        strokeGroup.add(strokeButton1);                        //按钮组添加按钮
        strokeGroup.add(strokeButton2);                       //按钮组添加按钮
        strokeGroup.add(strokeButton3);                       //按钮组添加按钮
        toolBar.add(strokeButton3);
        toolBar.addSeparator();                               //添加分割
        backgroundButton = new JButton("背景颜色");  //初始化按钮对象，并添加文本内容
        toolBar.add(backgroundButton);  //工具栏添加按钮
        foregroundButton = new JButton("前景颜色");
        toolBar.add(foregroundButton);
        toolBar.addSeparator();

        shapeButton = new JButton("图形");                              //初始化按钮对象，并添加文本内容
        toolBar.add(shapeButton);                                            //工具栏添加按钮

        clearButton = new JButton("清除");
        toolBar.add(clearButton);
        eraserButton = new JButton("橡皮");
        toolBar.add(eraserButton);

        JMenuBar menuBar = new JMenuBar();          //创建菜单栏
        setJMenuBar(menuBar);               //窗体载入菜单栏

        JMenu systemMenu = new JMenu("系统");     //初始化菜单对象，并添加文本内容
        menuBar.add(systemMenu);                     //菜单栏添加菜单对象
        shuiyinMenuItem = new JMenuItem("设置水印");
        systemMenu.add(shuiyinMenuItem);
        saveMenuItem = new JMenuItem("保存");   //初始化菜单对象，并添加文本内容
        systemMenu.add(saveMenuItem);
        systemMenu.addSeparator();                   //添加分割条
        exitMenuItem = new JMenuItem("退出");
        systemMenu.add(exitMenuItem);
        JMenu strokeMenu = new JMenu("线形");
        menuBar.add(strokeMenu);
        strokeMenuItem1 = new JMenuItem("细线");
        strokeMenu.add(strokeMenuItem1);
        strokeMenuItem2 = new JMenuItem("粗线");
        strokeMenu.add(strokeMenuItem2);
        strokeMenuItem3 = new JMenuItem("较粗");
        strokeMenu.add(strokeMenuItem3);

        JMenu colorMenu = new JMenu("颜色");
        menuBar.add(colorMenu);
        foregroundMenuItem = new JMenuItem("前景颜色");
        colorMenu.add(foregroundMenuItem);
        backgroundMenuItem = new JMenuItem("背景颜色");
        colorMenu.add(backgroundMenuItem);

        JMenu editMenu = new JMenu("编辑");
        menuBar.add(editMenu);
        clearMenuItem = new JMenuItem("清除");
        editMenu.add(clearMenuItem);
        eraserMenuItem = new JMenuItem("橡皮");
        editMenu.add(eraserMenuItem);

        //创建简笔画展示面板，并将本类当作它的父窗体
        pictureWindow = new PictureWindow(DrawPictureFrame.this);

    }

    /*
    * 为组件添加动作监听
    * */
    private void addListener() {
        //画板添加鼠标移动事件监听
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(final MouseEvent e) {                     //当鼠标拖拽时
                if (x > 0 && y > 0) {                                           //如果x和y存在鼠标记录
                    if (rubber) {                                               //如果橡皮标识为true，表示使用橡皮
                        g.setColor(backgroundColor);                          //绘图工具使用背景色
                        g.fillRect(x, y, 10, 10);               //在鼠标滑过的位置画填充的正方形
                    } else {
                        g.drawLine(x, y, e.getX(), e.getY());                  //在鼠标滑过的位置画直线
                    }
                }
                x = e.getX();       //上一次鼠标绘制点的横坐标
                y = e.getY();       //上一次鼠标绘制点的纵坐标
                canvas.repaint();   //更新画布
            }
            public void mouseMoved(final MouseEvent arg0){
                if(rubber){
                    //设置鼠标指针的形状为图片
                    Toolkit kit = Toolkit.getDefaultToolkit();    //获得系统默认的组件工具包
                    //利用工具包获取图片
                    Image img = kit.createImage("src/img/icon/鼠标橡皮.png");
                    //利用工具包创建一个自定义的光标对象
                    //参数为图片，光标热点(写成0,0就行)和光标描述字符串
                    Cursor c = kit.createCustomCursor(img, new Point(0,0), "clear");
                    setCursor(c);
                }else {
                    //设置鼠标指针为十字光标
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                }
            }
        });
        toolBar.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(final MouseEvent arg0){
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        canvas.addMouseListener(new MouseAdapter() {             //画板添加鼠标单击事件监听
            public void mouseReleased(final MouseEvent arg0) {     //当按键抬起时
                x = -1;               //将记录上一次鼠标绘制点的横坐标恢复为-1
                y = -1;               //将记录上一次鼠标绘制点的纵坐标恢复为-1
            }

            public void mousePressed(MouseEvent e) {
                if (drawShape) {
                    switch (shape.getType()) {
                        case Shapes.YUAN:          //如果是圆形
                            //计算坐标，让鼠标处于图形的中心位置
                            int yuanX = e.getX() - shape.getWidth() / 2;
                            int yuanY = e.getY() - shape.getHeigth() / 2;
                            //创建圆形图形，并指定坐标和宽高
                            Ellipse2D yuan = new Ellipse2D.Double(yuanX, yuanY, shape.getWidth(), shape.getHeigth());
                            g.draw(yuan);  //画图工具画此图形
                            break;
                        case Shapes.FANG:          //如果是方形
                            //计算坐标，让鼠标处于图形的中间位置
                            int fangX = e.getX() - shape.getWidth() / 2;
                            int fangY = e.getY() - shape.getHeigth() / 2;
                            //创建方形图形，并指定坐标和宽高
                            Rectangle2D fang = new Rectangle2D.Double(fangX, fangY, shape.getWidth(), shape.getHeigth());
                            g.draw(fang);
                            break;
                    }
                    canvas.repaint();
                    drawShape = false;
                }
            }
        });

        strokeButton1.addActionListener(new ActionListener() {   //细线按钮添加动作监听
            public void actionPerformed(final ActionEvent arg0) { //单击时
                //声明画笔的属性，粗细为1像素，线条末端无修饰，折线处呈尖角
                BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
                g.setStroke(bs);  //画图工具使用此画笔
            }
        });

        strokeButton2.addActionListener(new ActionListener() {  //粗线按钮动作监听
            public void actionPerformed(final ActionEvent arg0) {
                //声明画笔的属性，粗细为2像素，线条末端无修饰，折线处呈尖角
                BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
                g.setStroke(bs);
            }
        });

        strokeButton3.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                //声明画笔的属性，粗细为4像素，线条末端无修饰，折线处呈尖角
                BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
                g.setStroke(bs);
            }
        });

        backgroundButton.addActionListener(new ActionListener() {  //背景颜色按钮添加动作监听
            public void actionPerformed(final ActionEvent arg0) { //单击时
                //打开颜色选择对话框，参数依次为：父窗体、标题、默认选中的颜色（青色）
                Color bgColor = JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框", Color.CYAN);
                if (bgColor != null) {
                    backgroundColor = bgColor;
                }
                //背景色按钮也更换为这种背景色
                backgroundButton.setBackground(backgroundColor);
                g.setColor(backgroundColor);  //绘图工具使用背景色
                g.fillRect(0, 0, 570, 390);  //画一个背景颜色的方形填满整个画布
                g.setColor(foreColor);  //绘图工具使用前景色
                canvas.repaint();
                ;  //更新画布
            }
        });

        foregroundButton.addActionListener(new ActionListener() { //前景色颜色按钮添加动作监听
            public void actionPerformed(final ActionEvent arg0) {
                //打开颜色选择对话框，参数依次为：父窗体、标题、默认选中的颜色（青色)
                Color fColor = JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框", Color.CYAN);
                if (fColor != null) {
                    foreColor = fColor;
                }
                //前景色按钮的文字也更换为这种颜色
                foregroundButton.setForeground(foreColor);
                g.setColor(foreColor);  //绘图工具使用前景色
            }
        });

        clearButton.addActionListener(new ActionListener() {  //清除按钮添加动作监听
            public void actionPerformed(final ActionEvent arg0) {
                g.setColor(backgroundColor);  //绘图工具使用背景色
                g.fillRect(0, 0, 570, 390);  //画一个背景颜色的方形填满整个画布
                g.setColor(foreColor);  //绘图工具使用前景色
                canvas.repaint(); //更新画布
            }
        });

        eraserButton.addActionListener(new ActionListener() { //橡皮按钮添加监听动作
            public void actionPerformed(final ActionEvent arg0) {
                if (eraserButton.getText().equals("橡皮")) {
                    rubber = true;  //设置1橡皮标识为true
                    eraserButton.setText("画图"); //改变按钮上显示的文本为画图
                } else { //单击工具栏上的画图按钮，使用画笔
                    rubber = false;  //设置橡皮表示为false
                    eraserButton.setText("橡皮"); //改变按钮上显示的文本为橡皮
                    g.setColor(foreColor);  //设置画图对象为前景色
                }
            }
        });

        shapeButton.addActionListener(new ActionListener() { //图形按钮添加动作监听
            public void actionPerformed(ActionEvent e) {
                ShapeWindow shapeWindow = new ShapeWindow(DrawPictureFrame.this);
                int shapeButtonWidth = shapeButton.getWidth(); //获取图像按钮宽度
                int shapeWindowWidth = shapeWindow.getWidth(); //获取图形按钮高度
                int shapeButtonX = shapeButton.getX(); //获取图形按钮横坐标
                int shapeButtonY = shapeButton.getY(); //获取图形按钮纵坐标
                //计算图形组件横坐标，让组件与“图形”按钮居中对齐
                int shapeWindowX = getX() + shapeButtonX - (shapeWindowWidth - shapeButtonWidth) / 2;
                //计算图形组件纵坐标，让组件显示在“图形”按钮下方
                int shapeWindowY = getY() + shapeButtonY + 80;
                //设置图形组件坐标位置
                shapeWindow.setLocation(shapeWindowX, shapeWindowY);
                shapeWindow.setVisible(true);  //图形组件可见
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                addWatermark(); //添加水印
                DrawImageUtil.saveImage(DrawPictureFrame.this, image); //打印图片
            }
        });

        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        eraserMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (eraserMenuItem.getText().equals("橡皮")) {
                    rubber = true;
                    eraserMenuItem.setText("画图");
                    eraserButton.setText("画图");
                } else {
                    rubber = false;
                    eraserMenuItem.setText("橡皮");
                    eraserButton.setText("橡皮");
                    g.setColor(foreColor);
                }
            }
        });
        clearMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                g.setColor(backgroundColor);
                g.fillRect(0, 0, 570, 390);
                g.setColor(foreColor);
                canvas.repaint();
            }
        });
        strokeMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
                g.setStroke(bs);
                strokeButton1.setSelected(true);
            }
        });
        strokeMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                BasicStroke bs = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
                g.setStroke(bs);
                strokeButton2.setSelected(true);
            }
        });
        strokeMenuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                BasicStroke bs = new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
                g.setStroke(bs);
                strokeButton3.setSelected(true);
            }
        });
        foregroundMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Color fColor = JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框", Color.CYAN);
                if (fColor != null) {
                    foreColor = fColor;
                }
                foregroundButton.setForeground(foreColor);
                g.setColor(foreColor);
            }
        });
        backgroundMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Color bgColor = JColorChooser.showDialog(DrawPictureFrame.this, "选择颜色对话框", Color.CYAN);
                if (bgColor != null) {
                    backgroundButton.setBackground(backgroundColor);
                    g.setColor(backgroundColor);
                    g.fillRect(0, 0, 570, 390);
                    g.setColor(foreColor);
                    canvas.repaint();
                }
            }
        });
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addWatermark(); //添加水印
                DrawImageUtil.saveImage(DrawPictureFrame.this, image);
            }
        });
        shuiyinMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                shuiyin = JOptionPane.showInputDialog(DrawPictureFrame.this, "你想添加什么水印？");
                if (null == shuiyin) {
                    shuiyin = "";
                } else {
                    setTitle("画图程序(水印内容:[" + shuiyin + "])");
                }
            }
        });

        showPicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isVisible = pictureWindow.isVisible();              //获取简笔画展示窗口可见状态
                if (isVisible){
                    showPicButton.setText("展开简笔画");
                    pictureWindow.setVisible(false);
                }else {
                    showPicButton.setText("隐藏简笔画");
                    //重新指定简笔画展示窗口的显示位置
                    //横坐标=主窗体横坐标-简笔画窗体宽度-5
                    //纵坐标=主窗体纵坐标
                    pictureWindow.setLocation(getX() - pictureWindow.getWidth() - 5, getY());
                    pictureWindow.setVisible(true);
                }
            }
        });
    }

    /*
    * 恢复展开简笔画按钮的文本内容，此方法供和、简笔画面板的“隐藏”按钮调用
    * */
    public void initShowPicButton(){
        showPicButton.setText("展开简笔画");
    }

    /*
    * 添加水印
    * */
    private void addWatermark(){
        if (!"".equals(shuiyin.trim())){                        //如果水印字段不是空字符串
            g.rotate(Math.toRadians(-30));                      //将图片旋转-30弧度
            Font font = new Font("楷体", Font.BOLD, 72);      //设置字体
            g.setFont(font);                                              //载入字体
            g.setColor(Color.GRAY);                                       //使用灰色
            AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.4f);   //设置透明效果
            g.setComposite(alpha);                                        //使用透明效果
            g.drawString(shuiyin, 150, 500);                        //绘制文字
            canvas.repaint();                                              //面板重绘
            g.rotate(Math.toRadians(30));                                  //将旋转的图片再转回来
            alpha = AlphaComposite.SrcOver.derive(1f);                     //不透明效果
            g.setColor(foreColor);                                          //画笔恢复之前颜色
        }
    }

    /*
    * FrameGetShape接口实现类，用于获得图形空间返回被选中的图形
    * */
    public void getShape(Shapes shape){
        this.shape = shape; //将返回图形对象赋给类的全局变量
        drawShape = true; //画图形标识变量为true，说明现在鼠标画的是图形，而不是线条
    }

    /*
    * 程序运行主方法
    *
    * @param args - 运行时的参数，本程序用不到*/

    public static void main(String[] args){
        DrawPictureFrame frame = new DrawPictureFrame();    //创建窗体对象
        frame.setVisible(true);           //让窗体可见
    }
}