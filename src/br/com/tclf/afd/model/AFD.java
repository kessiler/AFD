package br.com.tclf.afd.model;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: KESSILER
 * Date: 09/11/13
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public class AFD {
    private Set<String> alphabet = new LinkedHashSet<String>();
    private Set<State> states = new LinkedHashSet<State>();
    private Set<Transition> transitions = new LinkedHashSet<Transition>();
    private State stateInitial;
    private Set<State> statesEnd = new LinkedHashSet<State>();

    public AFD() {
        this.alphabet.clear();
        this.states.clear();
        this.transitions.clear();
        this.statesEnd.clear();
    }

    public void addAlphabet(String character) {
        this.alphabet.add(character);
    }

    public void addState(State state) {
        if(state.isStateBegin()) {
            this.setStateInitial(state);
        }
        if(state.isStateEnd()) {
            this.addStatesEnd(state);
        }
        this.states.add(state);
    }

    public void addTransition(Transition transition) {
        this.transitions.add(transition);
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public Set<State> getStates() {
        return states;
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }

    public State getStateInitial() {
        return stateInitial;
    }

    private void setStateInitial(State stateInitial) {
        this.stateInitial = stateInitial;
    }

    public Set<State> getStatesEnd() {
        return statesEnd;
    }

    private void addStatesEnd(State statesEnd) {
        this.statesEnd.add(statesEnd);
    }

    public static AFD fromFile(String path) throws IOException {
        File file = new File(path);
        BufferedReader fileBuffer = new BufferedReader(new FileReader(file));
        AFD afd = new AFD();
        while (fileBuffer.ready()) {
            String lineFile = fileBuffer.readLine();
            if(!lineFile.isEmpty()) {
                if(lineFile.startsWith("A")) {
                    for (String simbolo : lineFile.substring(2).split(";")) {
                        afd.addAlphabet(simbolo.trim());
                    }
                } else if(lineFile.startsWith("E")) {
                    for (String states : lineFile.substring(2).split(";")) {
                        String[] stateArray = states.trim().split("-");
                        State state = new State(stateArray[0]);
                        for(int i = 1; i < stateArray.length; i++) {
                            if(stateArray[i].equals("I")) {
                                state.setStateBegin(Boolean.TRUE);
                            } else {
                                if(stateArray[i].equals("F")) {
                                    state.setStateEnd(Boolean.TRUE);
                                }
                            }
                        }
                        afd.addState(state);
                    }
                } else if(lineFile.startsWith("T")) {
                    for (String transition : lineFile.substring(2).split(";")) {
                        String transitionsLine[] = transition.split("-");
                        afd.addTransition(new Transition(new State(transitionsLine[0].trim()), transitionsLine[1].trim(), new State(transitionsLine[2].trim())));
                    }
                }
            }
        }
        return afd;
    }

    @Override
    public String toString() {
        StringBuilder afdSaveFile = new StringBuilder();
        afdSaveFile.append("A: ");
        for(String alphabet: this.getAlphabet()) {
            afdSaveFile.append(alphabet + "; ");
        }
        afdSaveFile.append(System.getProperty("line.separator"));
        afdSaveFile.append("E: ");
        for(State state: this.getStates()) {
            afdSaveFile.append(state.getName());
            if(state.isStateBegin()) afdSaveFile.append("-I");
            if(state.isStateEnd()) afdSaveFile.append("-F");
            afdSaveFile.append("; ");
        }
        afdSaveFile.append(System.getProperty("line.separator"));
        afdSaveFile.append("T: ");
        for(Transition transition: this.getTransitions()) {
            afdSaveFile.append(transition.getStateSource().getName() + "-" + transition.getCharacter() + "-" + transition.getStateDestination().getName() + "; ");
        }
        return afdSaveFile.toString();
    }

}