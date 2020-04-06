package bootcamp;

import net.corda.core.contracts.*;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;
import static net.corda.core.contracts.ContractsDSL.*;

import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireThat;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract{
    public static String ID = "java_bootcamp.TokenContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        List<ContractState> inputs = tx.getInputStates();
        List<ContractState> outputs = tx.getOutputStates();
        CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);
        //List<CommandWithParties<CommandData>> commands = tx.getCommands();


        if (!(command.getValue() instanceof Commands.Issue)) throw new IllegalArgumentException("Command must be of type issue");


        if (command.getValue() instanceof Commands.Issue) {
            requireThat(req -> {

                //shape
                req.using("inputs must be 0", inputs.size() ==0);
                req.using("outputs must be 0", outputs.size()==1);
                req.using("outputs is of type type TokenState", outputs.get(0) instanceof TokenState);

                // content
                TokenState tokenstate = (TokenState) outputs.get(0);
                req.using("Amount must be greater than 0", tokenstate.getAmount() > 0);

                //required signature
                req.using("Issuer has to be a required signer", command.getSigners().contains(tokenstate.getIssuer().getOwningKey()));

                return null;
            });
        } else if (command.getValue() instanceof Commands.Transfer) {


        }

        else {

        }
//    if (commands.get(0).getValue() instanceof Commands.Issue) {
//        if (inputs.size() != 0) throw new IllegalArgumentException("Input have to be 0");
//
//    }



    }

    public interface Commands extends CommandData {
        class Issue implements Commands { }
        class Transfer implements Commands { }
        class Lost implements Commands { }
    }
}