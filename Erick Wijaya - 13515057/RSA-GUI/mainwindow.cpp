#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QFileDialog>
#include <QMessageBox>

#include <string>
#include <fstream>
#include <cstdlib>
#include <ctime>
#include "rsa.h"

string readFile(char* filename){
    ifstream inputFile(filename);
    string plainText;

    while(inputFile.good()){
        char c = inputFile.get();
        if (c >= 0)
            plainText += c;
    }
    inputFile.close();

    return plainText;
}

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    setWindowTitle("RSA Encryption Decryption - Erick Wijaya - 13515057");

}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_ButtonBrowse_clicked()
{
    QString file = QFileDialog::getOpenFileName(this, tr("Open File"),"/path/to/file/",tr("Text Files (*.txt)"));
    if (!file.isEmpty()){
        fileName = file;
        ui->LabelFilename->setText(fileName);

        content = readFile(fileName.toLocal8Bit().data());
        ui->BrowserPlaintext->setText(QString::fromStdString(content));
        ui->BrowserPlaintext_2->clear();
        ui->BrowserPlaintext_3->clear();
    }
}

void MainWindow::on_ButtonGenKey_clicked()
{
    clock_t t = clock();
    security = RSA();
    t = clock() - t;
    eKey = security.getPublicKey();
    dKey = security.getPrivateKey();
    ui->BrowserPlaintext_2->clear();
    ui->BrowserPlaintext_3->clear();
    QString popUp = "Keys generation succeeded.\n";
    popUp.append("Public Key : " + QString::fromStdString(eKey.toString()) + "\n");
    popUp.append("Private Key: " + QString::fromStdString(dKey.toString()) + "\n");
    popUp.append("Time Elapsed: " + QString::number((double)t/CLOCKS_PER_SEC) + "s");
    QMessageBox::about(this, "Keys", popUp);
}

void MainWindow::on_ButtonEncrypt_clicked()
{
    if (fileName.isEmpty()){
        QMessageBox::warning(this, "Warning", "File not found. Tab Browse to upload text file.");
    }
    else if (eKey == 0){
        QMessageBox::warning(this, "Warning", "Keys not found. Tab Generate Keys before encrypting.");
    }
    else{
        clock_t t = clock();
        string cipherText = security.encrypt(content);
        t = clock() - t;
        ui->BrowserPlaintext_2->setText(QString::fromStdString(cipherText));
        QString message = "Encryption succeeded.\n";
        message.append("Time Elapsed: ");
        message.append(QString::number((double)t/CLOCKS_PER_SEC) + "s");
        QMessageBox::about(this, "Encryption", message);
    }
    ui->BrowserPlaintext_3->clear();
}

void MainWindow::on_ButtonDecrypt_clicked()
{
    if (ui->BrowserPlaintext_2->toPlainText().isEmpty()){
        QMessageBox::warning(this, "Warning", "Encryption must be done before decryption.");
    }
    else{
        clock_t t = clock();
        string plainText = security.decrypt(ui->BrowserPlaintext_2->toPlainText().toStdString());
        t = clock() - t;
        ui->BrowserPlaintext_3->setText(QString::fromStdString(plainText));
        QString message = "Decryption succeeded.\n";
        message.append("Time Elapsed: ");
        message.append(QString::number((double)t/CLOCKS_PER_SEC) + "s");
        QMessageBox::about(this, "Decryption", message);
    }
}

void MainWindow::on_actionReset_triggered()
{
    ui->LabelFilename->clear();
    ui->BrowserPlaintext->clear();
    ui->BrowserPlaintext_2->clear();
    ui->BrowserPlaintext_3->clear();
    security = RSA(0);
    eKey = 0; dKey = 0;
    fileName.clear();
    content.clear();
}
