import sqlite3

TableName = 'Storage'
# 格子編號, 物品種類, 內容數量, 圖片路徑
FieldName = ['s_Id', 'category', 'quantity', 'photoPath']


def createTable(c):
    # 格子編號, 物品種類, 內容數量, 圖片路徑
    c.execute('''CREATE TABLE IF NOT EXISTS {}(
                {} INT PRIMARY KEY NOT NULL,
                {} VARCHAR(20),
                {} INT,
                {} VARCHAR(100));'''.format(TableName, FieldName[0], FieldName[1], FieldName[2], FieldName[3]))
    print("Table created successfully")


def deleteTable(c):
    c.execute('''DROP TABLE {}'''.format(TableName))
    print("Table deleted successfully")


def insert(c, ID, category, quantity, photoPath):
    params = (ID, category, quantity, photoPath)
    c.execute("INSERT INTO {} VALUES (?, ?, ?, ?)".format(TableName), params)


def select(c):
    cursor = c.execute("SELECT * FROM {}".format(TableName))
    # cur.execute("select * from people where name_last=:who and age=:age", {"who": who, "age": age})
    for row in cursor:
        print(row)


def update(c, ID, category, quantity, photoPath):
    if category != None:
        c.execute("UPDATE {} SET category=:category WHERE s_ID=:ID".format(TableName), {"category": category, "ID": ID})
    if quantity != None:
        c.execute("UPDATE {} SET quantity=:quantity WHERE s_ID=:ID".format(TableName), {"quantity": quantity, "ID": ID})
    if photoPath != None:
        c.execute("UPDATE {} SET photoPath=:photoPath WHERE s_ID=:ID".format(TableName), {"photoPath": photoPath, "ID": ID})


if __name__ == '__main__':
    # 建立新的DB, 如果沒有會新增
    # with sqlite3.connect(':memory:') as conn: ..>寫在RAM
    with sqlite3.connect('test.db') as conn:
        print("Open database successfully")
        c = conn.cursor()

        # deleteTable(c)
        createTable(c)
        # insert(c, ID=2, category='redbox', quantity=1, photoPath='dataFromRasp')
        update(c, ID=2, category='bluebox', quantity=2, photoPath='dataFromRaspRRR')
        select(c)

        conn.commit()
