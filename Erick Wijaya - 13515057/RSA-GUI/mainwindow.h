#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include "rsa.h"

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    void on_ButtonBrowse_clicked();

    void on_ButtonGenKey_clicked();

    void on_ButtonEncrypt_clicked();

    void on_ButtonDecrypt_clicked();

    void on_actionReset_triggered();

private:
    Ui::MainWindow *ui;
    QString fileName;
    string content;

    RSA security{0};
    biginteger eKey;
    biginteger dKey;
};

#endif // MAINWINDOW_H
