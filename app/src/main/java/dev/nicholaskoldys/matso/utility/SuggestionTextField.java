package dev.nicholaskoldys.matso.utility;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Side;
import javafx.scene.control.*;
import java.util.*;

public class SuggestionTextField extends TextField {

    private SortedSet<String> items;
    private ContextMenu suggestionMenu;


    /**
     * Child of TextField with a context menu that becomes visible on typing.
     */
    public SuggestionTextField() {
        super();
        this.items = new TreeSet<>();
        this.suggestionMenu = new ContextMenu();
        textFieldlistener();
    }


    /**
     *  List will appear in context menu if typing text is matched.
     *
     * @param list for ContextMenu to pull suggestions from.
     */
    public void setSuggestionList(List<String> list) {
        this.items.addAll(list);
    }


    /**
     *  Whenever textField is changed or typed in, attempt to
     *  show the available suggestions, if any.
     */
    private void textFieldlistener() {
        this.textProperty().addListener( new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    String text = getText();

                    /* Determine if suggestions menu is shown */
                    if(text.isEmpty() && !SuggestionTextField.this.isFocused()) {
                        suggestionMenu.hide();
                    } else {

                        /* If text is contained in an items string, add it to the list */
                        LinkedList<String> suggestions = new LinkedList<>();
                        for(String str : items) {
                            if(str.toUpperCase().contains(text.trim().toUpperCase())) {
                                suggestions.add(str);
                            }
                        }

                        /* Don't show an empty suggestions menu */
                        if(suggestions.size() >= 0 && SuggestionTextField.this.isFocused()) {
                            addSuggestions(suggestions, 10);
                            suggestionMenu.show(SuggestionTextField.this, Side.BOTTOM, 0, 0);
                        } else {
                            suggestionMenu.hide();
                        }
                    }
                }
            }
        );
    }


    /**
     * Create MenuItems for the ContextMenu.
     * Give the ContextMenu onActionEvents, to load selected string into textField.
     *
     * @param textFieldText TextField to Pass string to.
     * @param amount Context Menu Max Content Size
     */
    private void addSuggestions(List<String> textFieldText, int amount) {
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int maxMenuSize = amount;
        int menuSize = Math.min(textFieldText.size(), maxMenuSize);
        for( int i = 0; i < menuSize; i++ ) {
            String menuItemText = textFieldText.get(i);
            Label menuItemLabel = new Label(menuItemText);
            CustomMenuItem item = new CustomMenuItem(menuItemLabel);
            item.setOnAction(event -> {SuggestionTextField.this.setText(menuItemText); suggestionMenu.hide();});
            menuItems.add(item);
        }
        suggestionMenu.getItems().clear();
        suggestionMenu.getItems().addAll(menuItems);
    }


    /**
     *  Special case method for showing 1 item on textField click.
     *  Useful for quick field entry.
     *   - used with LastBreakDown Date Entry
     */
    public void showSuggestionMenu() {
        LinkedList<String> suggestions = new LinkedList<>();
        for(String str : items) {
            suggestions.add(str);
        }
        addSuggestions(suggestions, 1);
        suggestionMenu.show(SuggestionTextField.this, Side.BOTTOM, 0, 0);
    }
}
