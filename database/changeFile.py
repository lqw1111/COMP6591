
def changeFile():
    string = ''
    rf = open("Production.stocks.txt", "r+", encoding='utf-8')  # rf是时时读取txt内容（已经修改过的），f是已经保存在内存中
    for orig in rf.readlines():
        orig = orig.strip('\n')
        string += orig + "," + "1" + "\n"
    rf.close()
    wf=open("list.txt",'w+',encoding='utf-8')  # wf的 w+打开是删除txt内容，写入rf中修改的内容
    wf.write(string)
    wf.close()

changeFile()