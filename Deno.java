import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Deno {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    int total = 68; 
    List<Integer> d = Arrays.asList(1, 2, 3, 4, 5); 
    List<Integer> denom = Arrays.asList(20, 10, 5, 2, 1);
	List<Integer> availableCoins;

    ArrayList<List<Integer>> history = new ArrayList<List<Integer>>();
    ArrayList<List<Integer>> withdraw = new ArrayList<List<Integer>>();
    ArrayList<List<Integer>> possibleCombo = new ArrayList<List<Integer>>();

    Deno() {
    	this.history.add(d);
    }

    public void start() throws IOException {
    	System.out.println("---------------------------");
        System.out.print("Command : ");
        String cmd = reader.readLine();
    	System.out.println("---------------------------");
        determinCommand(cmd);
    }

    private Boolean lowerCase(String validator, String cmd) {
    	return cmd.toLowerCase().contains(validator.toLowerCase());
    }

    private ArrayList<Integer> getAllDeno(String cmd) {
    	String[] deno = cmd.split(" ", 0);
    	String[] filtered = Arrays.copyOfRange(deno, 1, deno.length);

    	return Arrays
	    	.asList(filtered)
	    	.stream()
	    	.map(x -> Integer.parseInt(x))
	    	.collect(ArrayList<Integer>::new, ArrayList::add, ArrayList::addAll);
    }

    private Boolean posValid(int index, List<Integer> deposit, List<Integer> withdrawal) {
    	return deposit.get(index) - withdrawal.get(index) < 0;
    }

    private Boolean checkInvalidWithdrawal(List<Integer> deposit, List<Integer> withdrawal) {
    	Boolean isValid = true;

    	if(posValid(0, deposit, withdrawal)) {
    		isValid = false;
    		System.out.println("Insufficient denomination for value 20 index of 1" );
    	}
    	
    	if(posValid(1, deposit, withdrawal)) {
    		isValid = false;
    		System.out.println("Insufficient denomination for value 10 index of 2" );
    	}

    	if(posValid(2, deposit, withdrawal)) {
    		isValid = false;
	   		System.out.println("Insufficient denomination for value 5 index of 3" );
    	}

    	if(posValid(3, deposit, withdrawal)) {
    		isValid = false;
    		System.out.println("Insufficient denomination for value 2 index of 4" );
    	}

    	if(posValid(4, deposit, withdrawal)) {
    		isValid = false;
     		System.out.println("Insufficient denomination for value 1 index of 5" );
    	}

    	return isValid;
    }
   
    private List<Integer> getAvailableDeno(List<Integer> deposit, List<Integer> withdrawal) {
    	List<Integer> holder = Arrays.asList(0, 0, 0, 0, 0); 

		holder.set(0, deposit.get(0) -withdrawal.get(0));
		holder.set(1, deposit.get(1) -withdrawal.get(1));
		holder.set(2, deposit.get(2) -withdrawal.get(2));
		holder.set(3, deposit.get(3) -withdrawal.get(3));
		holder.set(4, deposit.get(4) -withdrawal.get(4));

		return holder;
    }

    private void show(Boolean withd) {
		AtomicInteger index = new AtomicInteger();

		List<Integer> deposit = transaction(history);
    	List<Integer> withdrawal = transaction(withdraw);

    	if(withd && !checkInvalidWithdrawal(deposit, withdrawal)) {
    		System.out.println("Please try again with valid denomination!");
    		withdraw.remove(withdraw.size() - 1);
    	}

    	List<Integer> holder = getAvailableDeno(deposit, withdrawal); 

       	List<Integer> multiplier = holder
    		.stream()
    		.map(a -> multiplier(a, index))
    		.collect(Collectors.toList());

    	index.set(0);

    	int total = multiplier
    		.stream()
    		.reduce(0, (subTotal, value) -> subTotal + value);

    	System.out.println("Denomination : " + holder.toString());
    	System.out.println("Total : " + total);
    }

    private List<Integer> transaction(ArrayList<List<Integer>> history) {
    	List<Integer> holder = Arrays.asList(0, 0, 0, 0, 0); 

    	history.forEach((element) -> {
    		holder.set(0, holder.get(0) +element.get(0)); 
    		holder.set(1, holder.get(1) +element.get(1)); 
    		holder.set(2, holder.get(2) +element.get(2)); 
    		holder.set(3, holder.get(3) +element.get(3)); 
    		holder.set(4, holder.get(4) +element.get(4)); 
    	});

    	return holder;
    }

    private Integer multiplier(int a, Integer index) {
		switch (index) {
			case 0: 
				return a * 20;
			case 1: 
				return a * 10;
			case 2: 
				return a * 5;
			case 3: 
				return a * 2;
			case 4:
				return a * 1;
			default: 
				return 0;
		}
    }

    private Integer multiplier(int a, AtomicInteger index) {
		return multiplier(a, index.getAndIncrement());
    }

    private void determinCommand(String cmd) throws IOException {
    	if(this.lowerCase("show", cmd)) {
    		this.show(false);
    		this.start();
    	} 
    	else if(this.lowerCase("put", cmd)) {
    		this.history.add(getAllDeno(cmd));
    		this.show(false);
    		this.start();
    	} 
    	else if(this.lowerCase("take", cmd)) {
    		this.withdraw.add(getAllDeno(cmd));
    		this.show(true);
    		this.start();
    	} 
    	else if(this.lowerCase("change", cmd)) {
    		int changeValue = Integer.parseInt(cmd.split(" ", 0)[1]);
    		changPos(changeValue);
    		this.show(true);
    		this.start();
    	} 
    	else if(this.lowerCase("quit", cmd)) 
    		System.out.println("Byerz!");
    	else {
    		System.out.println("Invalid Command Not Found [404]");
    		System.out.println("-" + cmd.split(" ", 0)[0] +"\n");
    		this.start();
    	}
    }

    private Integer getTotal(List<Integer> holder) {
		AtomicInteger index = new AtomicInteger();

    	List<Integer> v = holder
    		.stream()
    		.map(a -> multiplier(a, index))
    		.collect(Collectors.toList());

    	return v
    		.stream()
    		.reduce(0, (subTotal, value) -> subTotal + value);
    }

    private void changPos(Integer cv) {

 		List<Integer> available = getAvailableDeno(transaction(history), transaction(withdraw));

    	ArrayList<Integer> indices = new ArrayList<Integer>();

 		int i = 0;

 		for(; i < available.size();) {
 			if(available.get(i) != 0) {
 				if(cv >= multiplier(1, i)) {
 					int div = cv / multiplier(1, i);
 					indices.add(i);
 					System.out.println("deno: " + available.get(i) + " multiplier: " + multiplier(1, i));
 				}
 			}
 			i++;
 		}

 		availableCoins = indices
 			.stream()
 			.map(el -> multiplier(1, el))
 			.collect(Collectors.toList());

 		findDenomination(cv, available);
    }

    private ArrayList<List<Integer>> convertRep() {
    	return possibleCombo.stream().map(elem -> {
    		List<Integer> holder = Arrays.asList(0,0,0,0,0);
    		for(int i = 0; i < elem.size(); i++) {
    			switch (elem.get(i)) {
    				case 20: 
    					holder.set(0, holder.get(0)+ 1);
    					break;
    				case 10:
    					holder.set(1, holder.get(1)+ 1);
    					break;
    				case 5: 
    					holder.set(2, holder.get(2)+ 1);
    					break;
    				case 2:
    					holder.set(3, holder.get(3)+ 1);
    					break;
    				case 1:
    					holder.set(4, holder.get(4)+ 1);
    					break;
    				default:
    					break; 
    			}
    		}
    		return holder;
    	}).collect(ArrayList<List<Integer>>::new, ArrayList::add, ArrayList::addAll);
    } 

    private void findDenomination(int value, List<Integer> available) {
    	possibleCombo.clear();
      	System.out.println("Possible Denomination :" + findComb(availableCoins, availableCoins.size(), value, 0, ""));

      	ArrayList<List<Integer>> combi = convertRep();
      	if(combi.size() > 0) {
	      	for(List<Integer> elem : combi) {
	      		boolean poss = true;
	      		for(int i = 0; i < available.size(); i++) {
	      			if(available.get(i) - elem.get(i) < 0) {
	      				System.out.println("Possible change invalid:" + elem.toString() + " reason Insufficient denomination.");
	      				poss = false;
	      				break;
	      			} else poss = true; 
	      		}

	      		if(poss) {
	      			withdraw.add(elem);
	      		}
	      	}
	    } else 
	      	System.out.println("Insufficient denomination for value: "+ value);
    }

    private int findComb(List<Integer> coins, int n, int amount, int currentcoin, String combo) {
    	if(amount == 0) {
    		possibleCombo.add(
				Arrays.asList(combo
					.split(""))
					.stream()
					.map(elem -> Integer.parseInt(elem))
					.collect(Collectors.toList())
	    	);
    		return 1;
    	} else if(amount < 0)
    		return 0;
    	
    	int combs = 0;

    	for(int i = currentcoin; i < n; i++) {
    		String combse = combo + coins.get(i);

    		if(amount - coins.get(i) < 0) 
    			combse = combo;

    		combs += findComb(coins, n, amount - coins.get(i), i, combse);
    	}

    	return combs;
    }
}