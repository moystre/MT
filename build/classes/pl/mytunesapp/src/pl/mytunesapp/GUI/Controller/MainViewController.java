/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.mytunesapp.GUI.Controller;

import java.io.File;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import static java.util.Optional.empty;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Callback;
import pl.mytunesapp.BE.PlayList;
import pl.mytunesapp.BE.Song;
import pl.mytunesapp.BLL.MainAppManager;
import pl.mytunesapp.GUI.Model.MainAppModel;
import pl.mytunesapp.MainApp;
import pl.mytunesapp.GUI.Controller.EditSongDialogController;


/**
 * FXML Controller class
 *
 * @author MSI GS40 6QE
 */

public class MainViewController implements Initializable {
private MainApp mainApp = new MainApp();
private MainAppModel model = MainAppModel.getInstance();
private MainAppManager manager = new MainAppManager();
public static MainViewController ViewController;

    @FXML
    private Label playListTxtHolder;
    @FXML
    private Label songTxtHolder;
    @FXML
    private TableView<PlayList> playListTableView;
    @FXML
    private TableColumn<PlayList, String> playlistNameColumn;
    @FXML
    private TableColumn<PlayList, Integer> playlistSongsAmountColumn;
    @FXML
    private TableColumn<PlayList, String> playlistTimeColumn;
    @FXML
    private Button newPlaylistButton;
    @FXML
    private Button editPlaylistButton;
    @FXML
    private Button deletePlaylistButton;
    @FXML
    private Button playButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button previousButton;
    @FXML
    private Slider volume;
    @FXML
    private ListView<Song> listOfSongsInPlaylistView;
    @FXML
    private Button deleteSongFromPlaylistButton;
    @FXML
    private Label songPlayedLabel;
    @FXML
    private ProgressBar songProgress;
    @FXML
    private Button addSongToPlaylistButton;
    
    @FXML
    private Button moveSongDownButton;
    @FXML
    private Button moveSongUpButton;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Song> allSongsTableView;
    @FXML
    private TableColumn<Song, String> titleColumn;
    @FXML
    private TableColumn<Song, String> artistColumn;
    @FXML
    private TableColumn<Song, String> categoryColumn;
    @FXML
    private TableColumn<Song, Integer> timeColumn;
    @FXML
    private Button newSongButton;
    @FXML
    private Button deleteSongButton;
    @FXML
    private Button editSongButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button searchButton;


    ObservableList<Song> tableAllSongsContent =  MainAppModel.getInstance().getAllSongsList();
    ObservableList<Song> tableAllSongsContentFilter =  MainAppModel.getInstance().getDataAllSongsList();
    ObservableList<Song> listSongsInPlaylisContent =  MainAppModel.getInstance().getAllSongsInPlayListsList();
    ObservableList<PlayList> tableAllPlayListsContent =  MainAppModel.getInstance().getAllPlayListsList();


     MediaPlayer mediaPlayer;
     Song currentSong;
     String path;
    
     private void setMediaPlayer()
     {
           currentSong = allSongsTableView.getSelectionModel().getSelectedItem();
           path = currentSong.getFilePath();
            
            File bip = new File(path);
            Media hit;
        
           try
           {
               hit = new Media(bip.toURI().toURL().toString());
               mediaPlayer = new MediaPlayer(hit);
           } catch (MalformedURLException ex)
           {
               Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
           }
     }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableButtons();
        fixViews();
        fixListeners();
        fixSearchOption();
        ViewController=this;
        
        volume.valueProperty().addListener(new ChangeListener<Number>() 
         {
             @Override 
             public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
             {
                    mediaPlayer.setVolume(volume.getValue()/100);
                    //System.out.println(mediaPlayer.getVolume());
             }
        });
    }    
    public void setTableAllSongsContent(ObservableList<Song> list)
    {
       tableAllSongsContentFilter.setAll(list);
    }
    public void setListSongsInPlaylistContent(ObservableList<Song> list)
    {
       listSongsInPlaylisContent.setAll(list);
    }
    public void refreshViews()
    {
        listOfSongsInPlaylistView.setItems(model.getAllSongsFromPlaylist());
        allSongsTableView.itemsProperty().setValue(tableAllSongsContentFilter);
        allSongsTableView.refresh();
        playListTableView.refresh();
    }

        @FXML
    private void addSongToPlaylist(ActionEvent event) {
    model.addSongToPlaylistList(returnPlayListsTableSelection(), returnAllSongsTableSelection());

    }

    @FXML
    private void newPlaylist(ActionEvent event) {
         try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pl/mytunesapp/GUI/View/AddPlaylistDialogFXML.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));  
                stage.show();
        } catch(Exception e) {
           e.printStackTrace();
          }
    }

    @FXML
    private void editPlaylist(ActionEvent event) {
         try {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pl/mytunesapp/GUI/View/EditPlaylistDialogFXML.fxml"));
               // fxmlLoader.setController(new EditController(allSongsTableView.getSelectionModel().getSelectedItem()));
               
                Parent root2 = (Parent) fxmlLoader.load();
                
                EditPlaylistDialogController editcontroller = fxmlLoader.getController();
                editcontroller.setPlayList(playListTableView.getSelectionModel().getSelectedItem());
                Stage stage = new Stage();
                Scene scene = new Scene(root2);
                stage.setScene(scene);
                stage.show();
                
        } catch(Exception e) {
           e.printStackTrace();
          }
    }
    
    @FXML
    private void deletePlaylist(ActionEvent event) {
        PlayList selectedPlayList = playListTableView.getSelectionModel().getSelectedItem();
        if(selectedPlayList!=null)
        playListTableView.getItems().remove(selectedPlayList);
    }
    
    @FXML
    private void play(ActionEvent event) {
        
        //mediaPlayer.setAutoPlay(true);
        
         if(allSongsTableView.getSelectionModel().getSelectedItem() == currentSong)
         {
            if(mediaPlayer.getStatus()==MediaPlayer.Status.PLAYING)
                mediaPlayer.pause();
            else if(mediaPlayer.getStatus()==MediaPlayer.Status.PAUSED)
                mediaPlayer.play();
           
         }
        else if(allSongsTableView.getSelectionModel().getSelectedItem() != currentSong)
         {
           if(mediaPlayer.getStatus()!=MediaPlayer.Status.PLAYING)
               {   mediaPlayer.stop();
                   setMediaPlayer();
                   mediaPlayer.play();}
            else if(mediaPlayer.getStatus()!=MediaPlayer.Status.PAUSED)
            {       setMediaPlayer(); 
                   mediaPlayer.play();
     }}
        
    }

    @FXML
    private void next(ActionEvent event) {
       
       mediaPlayer.stop();
       allSongsTableView.getSelectionModel().selectNext();
       setMediaPlayer();
       mediaPlayer.play();
    }

    @FXML
    private void previous(ActionEvent event) {
        mediaPlayer.stop();
       allSongsTableView.getSelectionModel().selectPrevious();
       setMediaPlayer();
       mediaPlayer.play();
    }

    @FXML
    private void deleteSongFromPlaylist(ActionEvent event) {
        model.getAllSongsFromPlaylist().remove(listOfSongsInPlaylistView.getSelectionModel().getSelectedItem());
    }



    @FXML
    private void moveSongDown(ActionEvent event) {
    int selectedSongIndex = listOfSongsInPlaylistView.getSelectionModel().getSelectedIndex();
    Song tempSong = listSongsInPlaylisContent.get(selectedSongIndex+1);
    Song selectedSong = listSongsInPlaylisContent.get(selectedSongIndex);
    listOfSongsInPlaylistView.getItems().set(selectedSongIndex+1, selectedSong);
    listOfSongsInPlaylistView.getItems().set(selectedSongIndex, tempSong);
    refreshViews();
    }

    @FXML
    private void moveSongUp(ActionEvent event) {
    }

    @FXML
    private void newSong(ActionEvent event) {               
       try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pl/mytunesapp/GUI/View/AddSongDialogFXML.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root1));  
                stage.show();
        } catch(Exception e) {
           e.printStackTrace();
          }
       
    }

    private void clearAllSongsTableSelection() {
        allSongsTableView.getSelectionModel().clearSelection();
    }
    private void clearPlayListsTableSelection() {
        playListTableView.getSelectionModel().clearSelection();
    }
    public PlayList returnPlayListsTableSelection() {
        PlayList playlist =  playListTableView.getSelectionModel().getSelectedItem();
        if(playlist==null)
        return new PlayList();
        else
        return  playlist;
    }    
    public Song returnAllSongsTableSelection() {
        return allSongsTableView.getSelectionModel().getSelectedItem();
    }    
    public ObservableList<Song> getSongsInPlaylist()
            {
               return model.getAllSongsFromPlaylist();
            }
    @FXML
    private void deleteSong(ActionEvent event) {
        Song selectedSong = allSongsTableView.getSelectionModel().getSelectedItem();
        if(selectedSong!=null)
        model.removeSong(selectedSong);

    }
    
    

    @FXML
    private void editSong(ActionEvent event) {
         try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pl/mytunesapp/GUI/View/EditSongDialogFXML.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Song song = allSongsTableView.getSelectionModel().getSelectedItem();
                EditSongDialogController editcontroller = fxmlLoader.getController();
                editcontroller.setSong(song);
                Stage stage = new Stage();
                Scene scene = new Scene(root1);
                stage.setScene(scene);
                stage.show();
        } catch(Exception e) {
           e.printStackTrace();
          }
             
    }
    

    @FXML
    private void close(ActionEvent event) {
    }

    @FXML
    private void search(ActionEvent event) {
    }
     
    public void addSong(Song song)
    {
        if(allSongsTableView.isEmpty()) allSongsTableView.getSelectionModel().select(song);
        this.allSongsTableView.add(song);
        setMediaPlayer();
    }
    
    
    
      private void fixListeners(){
          
    playListTableView.getSelectionModel().selectedItemProperty().addListener(
        new ChangeListener<PlayList>() {
        @Override
        public void changed(ObservableValue<? extends PlayList> ov, PlayList t, PlayList t1) {
           refreshViews();
        }
    }
    );
    
    
    }

    private void fixViews(){
                allSongsTableView.itemsProperty().setValue(tableAllSongsContent);
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<Song, String>("Title")
        );
         artistColumn.setCellValueFactory(
                new PropertyValueFactory<Song, String>("Artist")
        );
          categoryColumn.setCellValueFactory(
                new PropertyValueFactory<Song, String>("Category")
        );
           timeColumn.setCellValueFactory(
                new PropertyValueFactory<Song, Integer>("Time")
        );
           
           playListTableView.itemsProperty().setValue(tableAllPlayListsContent);
           
           playlistNameColumn.setCellValueFactory(
                   new PropertyValueFactory<PlayList, String>("Name"));
           playlistTimeColumn.setCellValueFactory(
                   new PropertyValueFactory<PlayList, String>("Duration"));
           playlistSongsAmountColumn.setCellValueFactory(
                   new PropertyValueFactory<PlayList, Integer>("NumberOfSongs"));
           
           listOfSongsInPlaylistView.setCellFactory(new Callback<ListView<Song>, ListCell<Song>>() {

            @Override
            public ListCell<Song> call(ListView<Song> param) {
                ListCell<Song> cell = new ListCell<Song>() {

                    @Override
                    protected void updateItem(Song item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getTitle());
                        } else {
                            setText("");
                        }
                    }
                };
                return cell;
            }
        });
       
    }
    private void fixSearchOption(){
        searchField.textProperty().addListener(new ChangeListener<String>() 
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
            {
                manager.filterSongs(newValue, tableAllSongsContent);
                refreshViews();
            }
        });
        
    }
    private void disableButtons(){
        editPlaylistButton.disableProperty().bind(Bindings.isEmpty(playListTableView.getSelectionModel().getSelectedItems()));
        deletePlaylistButton.disableProperty().bind(Bindings.isEmpty(playListTableView.getSelectionModel().getSelectedItems()));
        editSongButton.disableProperty().bind(Bindings.isEmpty(allSongsTableView.getSelectionModel().getSelectedItems()));
        deleteSongButton.disableProperty().bind(Bindings.isEmpty(allSongsTableView.getSelectionModel().getSelectedItems()));
    }
}
