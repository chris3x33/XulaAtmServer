package v20170926.xulaAtmModel.result;

public class TransferResult extends Result {

    private String transferMsg;

    public TransferResult() {
    }

    public TransferResult(int status) {
        super(status);
    }

    public TransferResult(int status, String message) {
        super(status, message);
    }

    public String getTransferMsg() {
        return transferMsg;
    }

    public void setTransferMsg(String transferMsg) {
        this.transferMsg = transferMsg;
    }

}

