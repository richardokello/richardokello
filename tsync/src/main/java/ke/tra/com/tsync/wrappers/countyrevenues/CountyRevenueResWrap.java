package ke.tra.com.tsync.wrappers.countyrevenues;

import ke.tra.com.tsync.wrappers.countydevicesresponse.AccIds;
import ke.tra.com.tsync.wrappers.countydevicesresponse.CountyIds;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public class CountyRevenueResWrap {
    private BigInteger code;
    private String message;
    private Data data;
    private Timestamp timestamp;

    public BigInteger getCode(){
        return code;
    }
    public void setCode(BigInteger input){
        this.code = input;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String input){
        this.message = input;
    }
    public Data getData(){
        return data;
    }
    public void setData(Data input){
        this.data = input;
    }
    public Timestamp getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(Timestamp input){
        this.timestamp = input;
    }

    public static class Data{

        private List<Content> content;
        private Pageable pageable;
        private int totalPages;
        private int totalElements;
        private boolean last;
        private boolean first;
        private Sort sort;
        private int numberOfElements;
        private int size;
        private int number;
        private boolean empty;

        public List<Content> getContent(){
            return content;
        }
        public void setContent(List<Content> input){
            this.content = input;
        }
        public Pageable getPageable(){
            return pageable;
        }
        public void setPageable(Pageable input){
            this.pageable = input;
        }
        public int getTotalPages(){
            return totalPages;
        }
        public void setTotalPages(int input){
            this.totalPages = input;
        }
        public int getTotalElements(){
            return totalElements;
        }
        public void setTotalElements(int input){
            this.totalElements = input;
        }
        public boolean getLast(){
            return last;
        }
        public void setLast(boolean input){
            this.last = input;
        }
        public boolean getFirst(){
            return first;
        }
        public void setFirst(boolean input){
            this.first = input;
        }
        public Sort getSort(){
            return sort;
        }
        public void setSort(Sort input){
            this.sort = input;
        }
        public int getNumberOfElements(){
            return numberOfElements;
        }
        public void setNumberOfElements(int input){
            this.numberOfElements = input;
        }
        public int getSize(){
            return size;
        }
        public void setSize(int input){
            this.size = input;
        }
        public int getNumber(){
            return number;
        }
        public void setNumber(int input){
            this.number = input;
        }
        public boolean getEmpty(){
            return empty;
        }
        public void setEmpty(boolean input){
            this.empty = input;
        }
        public static class Content{
            private int id;
            private String action;
            private String actionStatus;
            private String intrash;
            private BigInteger createdAt;
            private String name;
            private int isParent;
            private int parentId;
            private String uniqueId;
            private String levels;
            private int accId;
            private AccIds accIds;
            private int countyId;
            private CountyIds countyIds;
            private File file;

            public int getId(){
                return id;
            }
            public void setId(int input){
                this.id = input;
            }
            public String getAction(){
                return action;
            }
            public void setAction(String input){
                this.action = input;
            }
            public String getActionStatus(){
                return actionStatus;
            }
            public void setActionStatus(String input){
                this.actionStatus = input;
            }
            public String getIntrash(){
                return intrash;
            }
            public void setIntrash(String input){
                this.intrash = input;
            }
            public BigInteger getCreatedAt(){
                return createdAt;
            }
            public void setCreatedAt(BigInteger input){
                this.createdAt = input;
            }
            public String getName(){
                return name;
            }
            public void setName(String input){
                this.name = input;
            }
            public int getIsParent(){
                return isParent;
            }
            public void setIsParent(int input){
                this.isParent = input;
            }
            public int getParentId(){
                return parentId;
            }
            public void setParentId(int input){
                this.parentId = input;
            }
            public String getUniqueId(){
                return uniqueId;
            }
            public void setUniqueId(String input){
                this.uniqueId = input;
            }
            public String getLevels(){
                return levels;
            }
            public void setLevels(String input){
                this.levels = input;
            }
            public int getAccId(){
                return accId;
            }
            public void setAccId(int input){
                this.accId = input;
            }
            public AccIds getAccIds(){
                return accIds;
            }
            public void setAccIds(AccIds input){
                this.accIds = input;
            }
            public int getCountyId(){
                return countyId;
            }
            public void setCountyId(int input){
                this.countyId = input;
            }
            public CountyIds getCountyIds(){
                return countyIds;
            }
            public void setCountyIds(CountyIds input){
                this.countyIds = input;
            }
            public File getFile(){
                return file;
            }
            public void setFile(File input){
                this.file = input;
            }
        }
        public static class File{
        }
    }

    public static class Sort{
        private boolean sorted;
        private boolean unsorted;
        private boolean empty;

        public boolean getSorted(){
            return sorted;
        }
        public void setSorted(boolean input){
            this.sorted = input;
        }
        public boolean getUnsorted(){
            return unsorted;
        }
        public void setUnsorted(boolean input){
            this.unsorted = input;
        }
        public boolean getEmpty(){
            return empty;
        }
        public void setEmpty(boolean input){
            this.empty = input;
        }
    }
    public static class Pageable{
        private Sort sort;
        private int pageSize;
        private int pageNumber;
        private int offset;
        private boolean paged;
        private boolean unpaged;

        public Sort getSort(){
            return sort;
        }
        public void setSort(Sort input){
            this.sort = input;
        }
        public int getPageSize(){
            return pageSize;
        }
        public void setPageSize(int input){
            this.pageSize = input;
        }
        public int getPageNumber(){
            return pageNumber;
        }
        public void setPageNumber(int input){
            this.pageNumber = input;
        }
        public int getOffset(){
            return offset;
        }
        public void setOffset(int input){
            this.offset = input;
        }
        public boolean getPaged(){
            return paged;
        }
        public void setPaged(boolean input){
            this.paged = input;
        }
        public boolean getUnpaged(){
            return unpaged;
        }
        public void setUnpaged(boolean input){
            this.unpaged = input;
        }
    }




}