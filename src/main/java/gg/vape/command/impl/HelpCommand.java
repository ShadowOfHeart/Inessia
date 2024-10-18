package gg.vape.command.impl;

import gg.vape.command.Command;
import gg.vape.command.CommandInfo;
import gg.vape.helpers.ChatUtil;

@CommandInfo(name = "help")
public class HelpCommand extends Command {

    @Override
    public void execute(String[] args) {
        super.execute(args);
        ChatUtil.print("������ ������: ");
        ChatUtil.print(".vclip <���������> - ������������ ������������");
        ChatUtil.print(".hclip <���������> - �������������� ������������");
        ChatUtil.print(".toggle <��� ������> - ��������/��������� ������");
        ChatUtil.print(".crash - ���� ������� � ������� ������");
        ChatUtil.print(".help - �������� ������ ������");
        ChatUtil.print(".config load <��� �������> - ��������� ������");
        ChatUtil.print(".config save <��� �������> - ��������� ������");
        ChatUtil.print(".config list - ������ ��������");
        ChatUtil.print(".friend <��� �����> - ��������/������� �����");
        ChatUtil.print(".bind <��� ������> <�������> - ��������� ������ �� �������");
        ChatUtil.print(".set <��� ������> <��� ���������> <��� ���������> <��������> - �������� ��������� ������");
        ChatUtil.print(".kick <���> - ������ ������ ����� ��������");
    }
}
