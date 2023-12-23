//StockFishHandler.startStockfish("C:\\Users\\farar\\OneDrive\\Desktop\\SC-Project\\project\\src\\stockfish\\stockfish-windows-x86-64-avx2.exe");
//System.out.println(StockFishHandler.getStockfishMove("e2e4"));
package chess;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StockFishHandler{
    private static Process stockfishProcess;
    private static BufferedReader stockfishInput;
    private static OutputStreamWriter stockfishOutput;

    public static void startStockfish(String stockfishPath) {
        try {
            ProcessBuilder builder = new ProcessBuilder(stockfishPath);
            builder.redirectErrorStream(true);
            stockfishProcess = builder.start();

            stockfishInput = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
            stockfishOutput = new OutputStreamWriter(stockfishProcess.getOutputStream());

            // Initialize Stockfish
            initializeStockfish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeStockfish() throws IOException {
        sendCommand("uci");
        sendCommand("isready");
    }

    private static void sendCommand(String command) throws IOException {
        stockfishOutput.write(command + "\n");
        stockfishOutput.flush();
    }

    public static String getStockfishMove(String move) {
        try {
            if (move != null && !move.isEmpty()) {
                sendCommand("position startpos moves " + move);
            }

            sendCommand("go depth 5");

            String response;
            while ((response = stockfishInput.readLine()) != null) {
                if (response.startsWith("bestmove")) {
                    String[] parts = response.split("\\s+");
                    if (parts.length > 1) {
                        return parts[1]; // Return the first word after "bestmove"
                    }
                    return response.substring(9); // Extracting the move from "bestmove" response
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeStockfish() {
        try {
            if (stockfishProcess != null) {
                sendCommand("quit");
                stockfishProcess.destroy();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
