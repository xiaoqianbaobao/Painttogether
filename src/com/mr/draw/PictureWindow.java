package com.mr.draw;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JWindow;
import com.mr.util.BackgroundPanel;

public class PictureWindow extends JWindow{
    private JButton changeButton;                               //更换图片按钮
    private JButton hiddenButton;                               //隐藏按钮
    private BackgroundPanel centerPanel;                        //展示图片的带背景面板
    File list[];                                                //图片文件数组
    int index;                                                  //当前选中文件索引
    DrawPictureFrame frame;                                     //父窗体

    /*
    * 构造方法
    * @param frame - 父窗体
    * */
    public PictureWindow(DrawPictureFrame frame){
        this.frame = frame;                                      //构造参数的值赋给父窗体
        setSize(400, 460);                         //设置窗体宽高
        init();                                             //初始化窗体组件
        addListener();                                      //给组件添加监听
    }

    /*
    * 组件初始化方法
    * */
    public void init(){
        Container c = getContentPane();                     //获取窗体主容器
        File dir = new File("src/img/picture");  //创建简笔画素材文件夹对象
        list = dir.listFiles();                             //获取文件夹里的所有文件
        //初始化背景面板，使用图片文件夹里的第一张简笔画
        centerPanel = new BackgroundPanel(getListImage());
        c.add(centerPanel, BorderLayout.CENTER);            //背景面板放到主容器中部
        FlowLayout flow = new FlowLayout(FlowLayout.RIGHT); //创建右对齐的流布局
        flow.setHgap(20);                                   //水平间隔20像素
        JPanel southPanel = new JPanel();                   //创建南部面板
        southPanel.setLayout(flow);                         //南部面板使用刚才创建好的流布局
        changeButton = new JButton("更换图片");         //实例化“更换图片”按钮
        southPanel.add(changeButton);                       //南部面板添加按钮
        hiddenButton = new JButton("隐藏");
        southPanel.add(hiddenButton);
        c.add(southPanel, BorderLayout.SOUTH);              //南部面板放到主容器的南部位置
    }

    /*
    * 添加监听
    * */
    private void addListener(){
        hiddenButton.addActionListener(new ActionListener() {       //隐藏按钮添加动作监听
            public void actionPerformed(ActionEvent e) {
                setVisible(false);                                  //本窗体不可见
                frame.initShowPicButton();                          //父窗体还原简笔画按钮的文本内容
            }
        });
        changeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                centerPanel.setImage(getListImage());                 //背景面板重新载入图片
            }
        });
    }

    /*
    * 获取图片文件夹下的图片，每次调用此方法，都会获得不同的文件对象
    * @reutrn 返回文件对象
    * */
    private Image getListImage(){
        String imgPath = list[index].getAbsolutePath();             //获取当前索引下的图片文件路径
        ImageIcon image = new ImageIcon(imgPath);                   //获取此图片文件的图标对象
        index++;
        if (index >= list.length){
            index = 0;
        }
        return image.getImage();
    }
}
