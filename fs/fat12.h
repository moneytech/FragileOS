#define ADR_DISKIMG 0x13400

struct FILEINFO {
    unsigned char name[8], ext[3], type;
    char reserve[10];
    unsigned short time, date, clustno;
    unsigned int size;
};

#define FILEINFO_SIZE 32
#define FILE_CONTENT_HEAD_ADDR 0x15800
#define DISK_SECTOR_SIZE 512
