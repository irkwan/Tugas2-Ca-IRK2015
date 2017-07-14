/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 5.9.1
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QAction>
#include <QtWidgets/QApplication>
#include <QtWidgets/QButtonGroup>
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QLabel>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenu>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QScrollBar>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QTextBrowser>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QVBoxLayout>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QAction *actionReset;
    QWidget *centralWidget;
    QVBoxLayout *verticalLayout;
    QHBoxLayout *horizontalLayout_6;
    QPushButton *ButtonBrowse;
    QLabel *LabelFilename;
    QPushButton *ButtonGenKey;
    QHBoxLayout *horizontalLayout;
    QTextBrowser *BrowserPlaintext;
    QScrollBar *verticalScrollBar;
    QHBoxLayout *horizontalLayout_2;
    QPushButton *ButtonEncrypt;
    QPushButton *ButtonDecrypt;
    QHBoxLayout *horizontalLayout_5;
    QHBoxLayout *horizontalLayout_3;
    QTextBrowser *BrowserPlaintext_2;
    QScrollBar *verticalScrollBar_2;
    QHBoxLayout *horizontalLayout_4;
    QTextBrowser *BrowserPlaintext_3;
    QScrollBar *verticalScrollBar_3;
    QMenuBar *menuBar;
    QMenu *menuFile;
    QToolBar *mainToolBar;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QStringLiteral("MainWindow"));
        MainWindow->resize(572, 488);
        actionReset = new QAction(MainWindow);
        actionReset->setObjectName(QStringLiteral("actionReset"));
        centralWidget = new QWidget(MainWindow);
        centralWidget->setObjectName(QStringLiteral("centralWidget"));
        verticalLayout = new QVBoxLayout(centralWidget);
        verticalLayout->setSpacing(6);
        verticalLayout->setContentsMargins(11, 11, 11, 11);
        verticalLayout->setObjectName(QStringLiteral("verticalLayout"));
        horizontalLayout_6 = new QHBoxLayout();
        horizontalLayout_6->setSpacing(6);
        horizontalLayout_6->setObjectName(QStringLiteral("horizontalLayout_6"));
        horizontalLayout_6->setContentsMargins(0, -1, -1, -1);
        ButtonBrowse = new QPushButton(centralWidget);
        ButtonBrowse->setObjectName(QStringLiteral("ButtonBrowse"));
        ButtonBrowse->setMinimumSize(QSize(75, 0));

        horizontalLayout_6->addWidget(ButtonBrowse);

        LabelFilename = new QLabel(centralWidget);
        LabelFilename->setObjectName(QStringLiteral("LabelFilename"));

        horizontalLayout_6->addWidget(LabelFilename);

        horizontalLayout_6->setStretch(0, 1);
        horizontalLayout_6->setStretch(1, 6);

        verticalLayout->addLayout(horizontalLayout_6);

        ButtonGenKey = new QPushButton(centralWidget);
        ButtonGenKey->setObjectName(QStringLiteral("ButtonGenKey"));

        verticalLayout->addWidget(ButtonGenKey);

        horizontalLayout = new QHBoxLayout();
        horizontalLayout->setSpacing(6);
        horizontalLayout->setObjectName(QStringLiteral("horizontalLayout"));
        BrowserPlaintext = new QTextBrowser(centralWidget);
        BrowserPlaintext->setObjectName(QStringLiteral("BrowserPlaintext"));

        horizontalLayout->addWidget(BrowserPlaintext);

        verticalScrollBar = new QScrollBar(centralWidget);
        verticalScrollBar->setObjectName(QStringLiteral("verticalScrollBar"));
        verticalScrollBar->setOrientation(Qt::Vertical);

        horizontalLayout->addWidget(verticalScrollBar);


        verticalLayout->addLayout(horizontalLayout);

        horizontalLayout_2 = new QHBoxLayout();
        horizontalLayout_2->setSpacing(6);
        horizontalLayout_2->setObjectName(QStringLiteral("horizontalLayout_2"));
        ButtonEncrypt = new QPushButton(centralWidget);
        ButtonEncrypt->setObjectName(QStringLiteral("ButtonEncrypt"));

        horizontalLayout_2->addWidget(ButtonEncrypt);

        ButtonDecrypt = new QPushButton(centralWidget);
        ButtonDecrypt->setObjectName(QStringLiteral("ButtonDecrypt"));

        horizontalLayout_2->addWidget(ButtonDecrypt);


        verticalLayout->addLayout(horizontalLayout_2);

        horizontalLayout_5 = new QHBoxLayout();
        horizontalLayout_5->setSpacing(6);
        horizontalLayout_5->setObjectName(QStringLiteral("horizontalLayout_5"));
        horizontalLayout_3 = new QHBoxLayout();
        horizontalLayout_3->setSpacing(6);
        horizontalLayout_3->setObjectName(QStringLiteral("horizontalLayout_3"));
        BrowserPlaintext_2 = new QTextBrowser(centralWidget);
        BrowserPlaintext_2->setObjectName(QStringLiteral("BrowserPlaintext_2"));

        horizontalLayout_3->addWidget(BrowserPlaintext_2);

        verticalScrollBar_2 = new QScrollBar(centralWidget);
        verticalScrollBar_2->setObjectName(QStringLiteral("verticalScrollBar_2"));
        verticalScrollBar_2->setOrientation(Qt::Vertical);

        horizontalLayout_3->addWidget(verticalScrollBar_2);


        horizontalLayout_5->addLayout(horizontalLayout_3);

        horizontalLayout_4 = new QHBoxLayout();
        horizontalLayout_4->setSpacing(6);
        horizontalLayout_4->setObjectName(QStringLiteral("horizontalLayout_4"));
        BrowserPlaintext_3 = new QTextBrowser(centralWidget);
        BrowserPlaintext_3->setObjectName(QStringLiteral("BrowserPlaintext_3"));

        horizontalLayout_4->addWidget(BrowserPlaintext_3);

        verticalScrollBar_3 = new QScrollBar(centralWidget);
        verticalScrollBar_3->setObjectName(QStringLiteral("verticalScrollBar_3"));
        verticalScrollBar_3->setOrientation(Qt::Vertical);

        horizontalLayout_4->addWidget(verticalScrollBar_3);


        horizontalLayout_5->addLayout(horizontalLayout_4);


        verticalLayout->addLayout(horizontalLayout_5);

        verticalLayout->setStretch(0, 1);
        verticalLayout->setStretch(1, 1);
        verticalLayout->setStretch(2, 3);
        verticalLayout->setStretch(3, 1);
        MainWindow->setCentralWidget(centralWidget);
        menuBar = new QMenuBar(MainWindow);
        menuBar->setObjectName(QStringLiteral("menuBar"));
        menuBar->setGeometry(QRect(0, 0, 572, 21));
        menuFile = new QMenu(menuBar);
        menuFile->setObjectName(QStringLiteral("menuFile"));
        MainWindow->setMenuBar(menuBar);
        mainToolBar = new QToolBar(MainWindow);
        mainToolBar->setObjectName(QStringLiteral("mainToolBar"));
        MainWindow->addToolBar(Qt::TopToolBarArea, mainToolBar);
        statusBar = new QStatusBar(MainWindow);
        statusBar->setObjectName(QStringLiteral("statusBar"));
        MainWindow->setStatusBar(statusBar);

        menuBar->addAction(menuFile->menuAction());
        menuFile->addAction(actionReset);

        retranslateUi(MainWindow);

        ButtonBrowse->setDefault(false);


        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QApplication::translate("MainWindow", "MainWindow", Q_NULLPTR));
        actionReset->setText(QApplication::translate("MainWindow", "Reset", Q_NULLPTR));
        ButtonBrowse->setText(QApplication::translate("MainWindow", "Browse", Q_NULLPTR));
        LabelFilename->setText(QString());
        ButtonGenKey->setText(QApplication::translate("MainWindow", "Generate Public and Private Keys", Q_NULLPTR));
        BrowserPlaintext->setHtml(QApplication::translate("MainWindow", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">\n"
"<html><head><meta name=\"qrichtext\" content=\"1\" /><style type=\"text/css\">\n"
"p, li { white-space: pre-wrap; }\n"
"</style></head><body style=\" font-family:'MS Shell Dlg 2'; font-size:8.25pt; font-weight:400; font-style:normal;\">\n"
"<p style=\"-qt-paragraph-type:empty; margin-top:0px; margin-bottom:0px; margin-left:0px; margin-right:0px; -qt-block-indent:0; text-indent:0px;\"><br /></p></body></html>", Q_NULLPTR));
        ButtonEncrypt->setText(QApplication::translate("MainWindow", "Encrypt", Q_NULLPTR));
        ButtonDecrypt->setText(QApplication::translate("MainWindow", "Decrypt", Q_NULLPTR));
        menuFile->setTitle(QApplication::translate("MainWindow", "File", Q_NULLPTR));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
