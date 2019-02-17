package com.prac.onlinesql.mq.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author ly
 * @create 2019-02-06 22:39
 **/
public class AcademicWorksEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //著作ID
    private Integer id;
    //所属用户
    private Long userId;
    //著作名称
    private String name;
    //出版社名称
    private String pressName;
    //书号
    private String bookNo;
    //出版时间
    private String publishTime;
    //出版版次
    private String printEdition;
    //类别
    private String classfiy;
    //教材文件号
    private String fileNo;
    //总字数(万字)
    private Double wordNumber;
    //学科分类
    private String subjectClassification;
    //外链
    private String elink;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;
    //详细内容
    private String detailContent;
    //附件
    private String attachment;
    //作者
    private String author;

    //所属用户
    private String username;
    private List<String> attList;
    //点击数
    private int clickNum;
    //背景图
    private String bgImg;
    //访问的URL
    private String accessUrl;

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg;
    }

    public int getClickNum() {
        return clickNum;
    }

    public void setClickNum(int clickNum) {
        this.clickNum = clickNum;
    }

    /**
     * 设置：著作ID
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * 获取：著作ID
     */
    public Integer getId() {
        return id;
    }

    public List<String> getAttList() {
        return attList;
    }

    public void setAttList(List<String> attList) {
        this.attList = attList;
    }

    /**
     * 设置：所属用户
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    /**
     * 获取：所属用户
     */
    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 设置：著作名称
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * 获取：著作名称
     */
    public String getName() {
        return name;
    }
    /**
     * 设置：出版社名称
     */
    public void setPressName(String pressName) {
        this.pressName = pressName;
    }
    /**
     * 获取：出版社名称
     */
    public String getPressName() {
        return pressName;
    }
    /**
     * 设置：书号
     */
    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }
    /**
     * 获取：书号
     */
    public String getBookNo() {
        return bookNo;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 设置：出版时间
     */
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
    /**
     * 获取：出版时间
     */
    public String getPublishTime() {
        return publishTime;
    }
    /**
     * 设置：出版版次
     */
    public void setPrintEdition(String printEdition) {
        this.printEdition = printEdition;
    }
    /**
     * 获取：出版版次
     */
    public String getPrintEdition() {
        return printEdition;
    }
    /**
     * 设置：类别
     */
    public void setClassfiy(String classfiy) {
        this.classfiy = classfiy;
    }
    /**
     * 获取：类别
     */
    public String getClassfiy() {
        return classfiy;
    }
    /**
     * 设置：教材文件号
     */
    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }
    /**
     * 获取：教材文件号
     */
    public String getFileNo() {
        return fileNo;
    }
    /**
     * 设置：总字数(万字)
     */
    public void setWordNumber(Double wordNumber) {
        this.wordNumber = wordNumber;
    }
    /**
     * 获取：总字数(万字)
     */
    public Double getWordNumber() {
        return wordNumber;
    }
    /**
     * 设置：学科分类
     */
    public void setSubjectClassification(String subjectClassification) {
        this.subjectClassification = subjectClassification;
    }
    /**
     * 获取：学科分类
     */
    public String getSubjectClassification() {
        return subjectClassification;
    }
    /**
     * 设置：外链
     */
    public void setElink(String elink) {
        this.elink = elink;
    }
    /**
     * 获取：外链
     */
    public String getElink() {
        return elink;
    }
    /**
     * 设置：创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    /**
     * 获取：创建时间
     */
    public String getCreateTime() {
        return createTime;
    }
    /**
     * 设置：更新时间
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * 获取：更新时间
     */
    public String getUpdateTime() {
        return updateTime;
    }
    /**
     * 设置：详细内容
     */
    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }
    /**
     * 获取：详细内容
     */
    public String getDetailContent() {
        return detailContent;
    }
    /**
     * 设置：附件
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
    /**
     * 获取：附件
     */
    public String getAttachment() {
        return attachment;
    }

    @Override
    public String toString() {
        return "AcademicWorksEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", pressName='" + pressName + '\'' +
                ", bookNo='" + bookNo + '\'' +
                ", publishTime='" + publishTime + '\'' +
                ", printEdition='" + printEdition + '\'' +
                ", classfiy='" + classfiy + '\'' +
                ", fileNo='" + fileNo + '\'' +
                ", wordNumber=" + wordNumber +
                ", subjectClassification='" + subjectClassification + '\'' +
                ", elink='" + elink + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", detailContent='" + detailContent + '\'' +
                ", attachment='" + attachment + '\'' +
                ", author='" + author + '\'' +
                ", username='" + username + '\'' +
                ", attList=" + attList +
                ", clickNum=" + clickNum +
                ", bgImg='" + bgImg + '\'' +
                ", accessUrl='" + accessUrl + '\'' +
                '}';
    }
}

