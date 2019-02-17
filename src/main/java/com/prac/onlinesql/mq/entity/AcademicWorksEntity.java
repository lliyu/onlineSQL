package com.prac.onlinesql.mq.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author ly
 * @create 2019-02-06 22:39
 **/
public class AcademicWorksEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    //����ID
    private Integer id;
    //�����û�
    private Long userId;
    //��������
    private String name;
    //����������
    private String pressName;
    //���
    private String bookNo;
    //����ʱ��
    private String publishTime;
    //������
    private String printEdition;
    //���
    private String classfiy;
    //�̲��ļ���
    private String fileNo;
    //������(����)
    private Double wordNumber;
    //ѧ�Ʒ���
    private String subjectClassification;
    //����
    private String elink;
    //����ʱ��
    private String createTime;
    //����ʱ��
    private String updateTime;
    //��ϸ����
    private String detailContent;
    //����
    private String attachment;
    //����
    private String author;

    //�����û�
    private String username;
    private List<String> attList;
    //�����
    private int clickNum;
    //����ͼ
    private String bgImg;
    //���ʵ�URL
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
     * ���ã�����ID
     */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
     * ��ȡ������ID
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
     * ���ã������û�
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    /**
     * ��ȡ�������û�
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
     * ���ã���������
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * ��ȡ����������
     */
    public String getName() {
        return name;
    }
    /**
     * ���ã�����������
     */
    public void setPressName(String pressName) {
        this.pressName = pressName;
    }
    /**
     * ��ȡ������������
     */
    public String getPressName() {
        return pressName;
    }
    /**
     * ���ã����
     */
    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }
    /**
     * ��ȡ�����
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
     * ���ã�����ʱ��
     */
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
    /**
     * ��ȡ������ʱ��
     */
    public String getPublishTime() {
        return publishTime;
    }
    /**
     * ���ã�������
     */
    public void setPrintEdition(String printEdition) {
        this.printEdition = printEdition;
    }
    /**
     * ��ȡ��������
     */
    public String getPrintEdition() {
        return printEdition;
    }
    /**
     * ���ã����
     */
    public void setClassfiy(String classfiy) {
        this.classfiy = classfiy;
    }
    /**
     * ��ȡ�����
     */
    public String getClassfiy() {
        return classfiy;
    }
    /**
     * ���ã��̲��ļ���
     */
    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }
    /**
     * ��ȡ���̲��ļ���
     */
    public String getFileNo() {
        return fileNo;
    }
    /**
     * ���ã�������(����)
     */
    public void setWordNumber(Double wordNumber) {
        this.wordNumber = wordNumber;
    }
    /**
     * ��ȡ��������(����)
     */
    public Double getWordNumber() {
        return wordNumber;
    }
    /**
     * ���ã�ѧ�Ʒ���
     */
    public void setSubjectClassification(String subjectClassification) {
        this.subjectClassification = subjectClassification;
    }
    /**
     * ��ȡ��ѧ�Ʒ���
     */
    public String getSubjectClassification() {
        return subjectClassification;
    }
    /**
     * ���ã�����
     */
    public void setElink(String elink) {
        this.elink = elink;
    }
    /**
     * ��ȡ������
     */
    public String getElink() {
        return elink;
    }
    /**
     * ���ã�����ʱ��
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    /**
     * ��ȡ������ʱ��
     */
    public String getCreateTime() {
        return createTime;
    }
    /**
     * ���ã�����ʱ��
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * ��ȡ������ʱ��
     */
    public String getUpdateTime() {
        return updateTime;
    }
    /**
     * ���ã���ϸ����
     */
    public void setDetailContent(String detailContent) {
        this.detailContent = detailContent;
    }
    /**
     * ��ȡ����ϸ����
     */
    public String getDetailContent() {
        return detailContent;
    }
    /**
     * ���ã�����
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
    /**
     * ��ȡ������
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

