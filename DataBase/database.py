import sqlite3

TableName = 'Storage'
# 格子編號, 物品種類, 內容數量, 圖片路徑
FieldName = ['s_Id', 'category', 'quantity', 'photoPath']

# class dbmanager(sqlite3):
#     def __init__(self):
#         pass
#     # def __enter__(self):
#     #     pass
#     # def __exit__(self, exc_type, exc_val, exc_tb):
#     #     pass
#     # def createCursor(self):
#     #     conn = sqlite3.connect('test.db')

def createCursor():
    conn = sqlite3.connect('test.db')

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

def deleteRow(self, c, ID):
    c.execute('''DELETE FROM {} WHERE 
                {} :=ID;
                '''.format(TableName, FieldName[0]), {"ID": ID})


def insert(c, ID, category=None, quantity=None, photoPath=None):
    params = (ID, category, quantity, photoPath)
    c.execute("INSERT INTO {} VALUES (?, ?, ?, ?)".format(TableName), params)


def select(c):
    cursor = c.execute("SELECT * FROM {}".format(TableName))
    # cur.execute("select * from people where name_last=:who and age=:age", {"who": who, "age": age})
    # for row in cursor:
    #     print(row)
    print(cursor.fetchall())

def update(c, ID, category=None, quantity=None, photoPath=None):
    if category != None:
        c.execute("UPDATE {} SET category=:category WHERE s_ID=:ID".format(TableName), {"category": category, "ID": ID})
    if quantity != None:
        c.execute("UPDATE {} SET quantity=:quantity WHERE s_ID=:ID".format(TableName), {"quantity": quantity, "ID": ID})
    if photoPath != None:
        c.execute("UPDATE {} SET photoPath=:photoPath WHERE s_ID=:ID".format(TableName), {"photoPath": photoPath, "ID": ID})

def initDB(c, number):
    for i in range(number):
        insert(c, ID=i+1)


if __name__ == '__main__':
    with sqlite3.connect('test.db') as conn:
        print("Open database successfully")
        c = conn.cursor()

        deleteTable(c)
        createTable(c)
        initDB(c, 9)
        insert(c, ID=2, category='redbox', quantity=1, photoPath='dataFromRasp')
        update(c, ID=2, category='bluebox', quantity=2, photoPath='dataFromRaspRRR')
        select(c)

        conn.commit()
    with dbmanager.connect('test.db', ) as conn:
        pass
